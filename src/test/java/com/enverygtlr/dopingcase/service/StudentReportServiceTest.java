package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Choice;
import com.enverygtlr.dopingcase.domain.entity.StudentAnswer;
import com.enverygtlr.dopingcase.domain.response.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentReportServiceTest {

    @Mock
    private StudentService studentService;

    @Mock
    private TestAttendanceService testAttendanceService;

    @Mock
    private TestService testService;

    @Mock
    private StudentAnswerService studentAnswerService;

    @Mock
    private ChoiceService choiceService;

    @InjectMocks
    private StudentReportService studentReportService;

    @Test
    void getStudentReport_shouldReturnReport() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID correctChoiceId = UUID.randomUUID();
        UUID wrongChoiceId = UUID.randomUUID();

        StudentResponse student = StudentResponse.builder()
                .id(studentId)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .studentNo(123L)
                .build();

        QuestionResponse question1 = new QuestionResponse(questionId, testId, "Q1", List.of());
        QuestionResponse question2 = new QuestionResponse(UUID.randomUUID(), testId, "Q2", List.of());

        TestResponse test = new TestResponse(testId, "Test Title", List.of(question1, question2));

        StudentAnswer correctAnswer = StudentAnswer.builder()
                .studentId(studentId)
                .testId(testId)
                .questionId(questionId)
                .choiceId(correctChoiceId)
                .build();

        StudentAnswer wrongAnswer = StudentAnswer.builder()
                .studentId(studentId)
                .testId(testId)
                .questionId(UUID.randomUUID())
                .choiceId(wrongChoiceId)
                .build();

        Choice correctChoice = Choice.builder()
                .questionId(questionId)
                .isCorrectChoice(true)
                .build();
        correctChoice.setId(correctChoiceId);


        Choice wrongChoice = Choice.builder()
                .questionId(UUID.randomUUID())
                .isCorrectChoice(false)
                .build();
        wrongChoice.setId(wrongChoiceId);

        when(studentService.getStudent(studentId)).thenReturn(student);
        when(testAttendanceService.getAttendedTestIds(studentId)).thenReturn(List.of(testId));
        when(testService.getTestById(testId)).thenReturn(test);
        when(studentAnswerService.getAnswersByStudentAndTest(studentId, testId))
                .thenReturn(List.of(correctAnswer, wrongAnswer));
        when(choiceService.getChoicesByIds(List.of(correctChoiceId, wrongChoiceId)))
                .thenReturn(List.of(correctChoice, wrongChoice));

        // When
        StudentReportResponse report = studentReportService.getStudentReport(studentId);

        // Then
        assertEquals(studentId, report.studentId());
        assertEquals("John Doe", report.fullName());
        assertEquals("john@example.com", report.email());
        assertEquals(123L, report.studentNo());

        assertEquals(1, report.performances().size());

        StudentTestPerformanceResponse performance = report.performances().get(0);
        assertEquals(testId, performance.testId());
        assertEquals("Test Title", performance.title());
        assertEquals(1, performance.trueCount());
        assertEquals(1, performance.wrongCount());
        assertEquals(0, performance.emptyCount());
    }
}
