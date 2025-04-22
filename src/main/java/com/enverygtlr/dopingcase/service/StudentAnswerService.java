package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.config.CacheNames;
import com.enverygtlr.dopingcase.domain.entity.StudentAnswer;
import com.enverygtlr.dopingcase.domain.request.StudentAnswerRequest;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.repository.StudentAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;
    private final TestAttendanceService testAttendanceService;
    private final QuestionService questionService;
    private final ChoiceService choiceService;

    @CacheEvict(cacheNames = CacheNames.REPORT_CACHE, key = "#request.studentId()")
    @Transactional
    public void submitAnswer(StudentAnswerRequest request) {
        UUID studentId = request.studentId();
        UUID testId = request.testId();
        UUID questionId = request.questionId();
        UUID choiceId = request.choiceId();

        testAttendanceService.checkStudentAttendedTest(studentId, testId);
        questionService.checkQuestionBelongsToTest(questionId, testId);
        choiceService.checkChoiceBelongsToQuestion(choiceId, questionId);

        if (studentAnswerRepository.existsByStudentIdAndQuestionId(studentId, questionId)) {
            throw ValidationException.duplicateAnswer(studentId.toString(), questionId.toString());
        }

        StudentAnswer answer = StudentAnswer.builder()
                .studentId(studentId)
                .testId(testId)
                .questionId(questionId)
                .choiceId(choiceId)
                .build();

        studentAnswerRepository.save(answer);
    }

    @CacheEvict(cacheNames = CacheNames.REPORT_CACHE, key = "#request.studentId()")
    @Transactional
    public void changeAnswer(StudentAnswerRequest request) {
        UUID studentId = request.studentId();
        UUID testId = request.testId();
        UUID questionId = request.questionId();
        UUID choiceId = request.choiceId();

        testAttendanceService.checkStudentAttendedTest(studentId, testId);
        questionService.checkQuestionBelongsToTest(questionId, testId);
        choiceService.checkChoiceBelongsToQuestion(choiceId, questionId);

        StudentAnswer existing = studentAnswerRepository.findByStudentIdAndQuestionId(studentId, questionId)
                .orElseThrow(() -> new NotFoundException(questionId.toString()));

        existing.setChoiceId(choiceId);
        studentAnswerRepository.save(existing);
    }


    public List<StudentAnswer> getAnswersByStudentAndTest(UUID studentId, UUID testId) {
        return studentAnswerRepository.findAllByStudentIdAndTestId(studentId, testId);
    }

}