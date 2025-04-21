package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Choice;
import com.enverygtlr.dopingcase.domain.entity.Student;
import com.enverygtlr.dopingcase.domain.entity.StudentAnswer;
import com.enverygtlr.dopingcase.domain.response.StudentReportResponse;
import com.enverygtlr.dopingcase.domain.response.StudentResponse;
import com.enverygtlr.dopingcase.domain.response.StudentTestPerformanceResponse;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentReportService {
    private final StudentService studentService;
    private final TestAttendanceService testAttendanceService;
    private final TestService testService;
    private final QuestionService questionService;
    private final StudentAnswerService studentAnswerService;
    private final ChoiceService choiceService;

    public StudentReportResponse getStudentReport(UUID studentId) {
        StudentResponse studentResponse = studentService.getStudent(studentId);

        var attendedTestIds = testAttendanceService.getAttendedTestIds(studentId);

        List<StudentTestPerformanceResponse> performances = attendedTestIds.stream()
                .map(testId -> {
                    var testResponse = testService.getTestById(testId);
                    var questions = testResponse.questions();
                    var answers = studentAnswerService.getAnswersByStudentAndTest(studentId, testId);

                    var choiceIds = answers.stream().map(StudentAnswer::getChoiceId).toList();
                    var selectedChoices = choiceService.getChoicesByIds(choiceIds);

                    long trueCount = selectedChoices.stream()
                            .filter(Choice::getIsCorrectChoice)
                            .count();

                    long answeredQuestionCount = answers.size();
                    long wrongCount = answeredQuestionCount - trueCount;
                    long emptyCount = questions.size() - answeredQuestionCount;

                    return new StudentTestPerformanceResponse(
                            testResponse.testId(),
                            testResponse.title(),
                            trueCount,
                            wrongCount,
                            emptyCount
                    );

                })
                .toList();

        return new StudentReportResponse(
                studentResponse.id(),
                studentResponse.firstName() + " " + studentResponse.lastName(),
                studentResponse.email(),
                studentResponse.studentNo(),
                performances
        );
    }
}
