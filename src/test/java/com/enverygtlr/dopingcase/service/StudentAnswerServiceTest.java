package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.StudentAnswer;
import com.enverygtlr.dopingcase.domain.request.StudentAnswerRequest;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.repository.StudentAnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentAnswerServiceTest {

    @Mock
    private StudentAnswerRepository studentAnswerRepository;

    @Mock
    private TestAttendanceService testAttendanceService;

    @Mock
    private QuestionService questionService;

    @Mock
    private ChoiceService choiceService;

    @InjectMocks
    private StudentAnswerService studentAnswerService;

    @Test
    void submitAnswer_shouldSucceed() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID choiceId = UUID.randomUUID();

        StudentAnswerRequest request = new StudentAnswerRequest(studentId, testId, questionId, choiceId);

        when(studentAnswerRepository.existsByStudentIdAndQuestionId(studentId, questionId)).thenReturn(false);

        // When
        studentAnswerService.submitAnswer(request);

        // Then
        verify(testAttendanceService).checkStudentAttendedTest(studentId, testId);
        verify(questionService).checkQuestionBelongsToTest(questionId, testId);
        verify(choiceService).checkChoiceBelongsToQuestion(choiceId, questionId);
        verify(studentAnswerRepository).save(any(StudentAnswer.class));
    }

    @Test
    void submitAnswer_shouldThrowIfAlreadyAnswered() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID choiceId = UUID.randomUUID();

        StudentAnswerRequest request = new StudentAnswerRequest(studentId, testId, questionId, choiceId);

        when(studentAnswerRepository.existsByStudentIdAndQuestionId(studentId, questionId)).thenReturn(true);

        // When - Then
        assertThrows(ValidationException.class, () -> studentAnswerService.submitAnswer(request));
        verify(studentAnswerRepository, never()).save(any());
    }

    @Test
    void changeAnswer_shouldSucceed() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID oldChoiceId = UUID.randomUUID();
        UUID newChoiceId = UUID.randomUUID();

        StudentAnswerRequest request = new StudentAnswerRequest(studentId, testId, questionId, newChoiceId);

        StudentAnswer existingAnswer = StudentAnswer.builder()
                .studentId(studentId)
                .testId(testId)
                .questionId(questionId)
                .choiceId(oldChoiceId)
                .build();

        when(studentAnswerRepository.findByStudentIdAndQuestionId(studentId, questionId))
                .thenReturn(Optional.of(existingAnswer));

        // When
        studentAnswerService.changeAnswer(request);

        // Then
        assertEquals(newChoiceId, existingAnswer.getChoiceId());
        verify(studentAnswerRepository).save(existingAnswer);
        verify(testAttendanceService).checkStudentAttendedTest(studentId, testId);
        verify(questionService).checkQuestionBelongsToTest(questionId, testId);
        verify(choiceService).checkChoiceBelongsToQuestion(newChoiceId, questionId);
    }

    @Test
    void changeAnswer_shouldThrowIfNotFound() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID choiceId = UUID.randomUUID();

        StudentAnswerRequest request = new StudentAnswerRequest(studentId, testId, questionId, choiceId);

        when(studentAnswerRepository.findByStudentIdAndQuestionId(studentId, questionId))
                .thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> studentAnswerService.changeAnswer(request));
        verify(studentAnswerRepository, never()).save(any());
    }

    @Test
    void getAnswersByStudentAndTest_shouldReturnList() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        List<StudentAnswer> expectedAnswers = List.of(new StudentAnswer(), new StudentAnswer());

        when(studentAnswerRepository.findAllByStudentIdAndTestId(studentId, testId)).thenReturn(expectedAnswers);

        // When
        List<StudentAnswer> result = studentAnswerService.getAnswersByStudentAndTest(studentId, testId);

        // Then
        assertEquals(expectedAnswers, result);
        verify(studentAnswerRepository).findAllByStudentIdAndTestId(studentId, testId);
    }
}
