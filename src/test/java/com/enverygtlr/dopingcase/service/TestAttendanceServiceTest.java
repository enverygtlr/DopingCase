package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.TestAttendance;
import com.enverygtlr.dopingcase.domain.request.TestAttendanceRequest;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.repository.TestAttendanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestAttendanceServiceTest {

    @Mock
    private TestAttendanceRepository attendanceRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private TestService testService;

    @InjectMocks
    private TestAttendanceService testAttendanceService;

    @Test
    void attendTest_shouldSucceed() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestAttendanceRequest request = new TestAttendanceRequest(studentId, testId);

        when(attendanceRepository.existsByStudentIdAndTestId(studentId, testId)).thenReturn(false);

        // When
        testAttendanceService.attendTest(request);

        // Then
        verify(testService).checkTestExists(testId);
        verify(studentService).checkStudentExists(studentId);
        verify(attendanceRepository).save(any(TestAttendance.class));
    }

    @Test
    void attendTest_shouldThrowIfAlreadyAttended() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestAttendanceRequest request = new TestAttendanceRequest(studentId, testId);

        when(attendanceRepository.existsByStudentIdAndTestId(studentId, testId)).thenReturn(true);

        // When - Then
        assertThrows(ValidationException.class, () -> testAttendanceService.attendTest(request));
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    void removeAttendance_shouldSucceed() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        TestAttendance attendance = new TestAttendance();
        attendance.setStudentId(studentId);
        attendance.setTestId(testId);

        when(attendanceRepository.findByStudentIdAndTestId(studentId, testId))
                .thenReturn(Optional.of(attendance));

        // When
        testAttendanceService.removeAttendance(testId, studentId);

        // Then
        verify(testService).checkTestExists(testId);
        verify(studentService).checkStudentExists(studentId);
        verify(attendanceRepository).delete(attendance);
    }

    @Test
    void removeAttendance_shouldThrowIfNotFound() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();

        when(attendanceRepository.findByStudentIdAndTestId(studentId, testId))
                .thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class,
                () -> testAttendanceService.removeAttendance(testId, studentId));
        verify(attendanceRepository, never()).delete(any());
    }

    @Test
    void checkStudentAttendedTest_shouldDoNothingIfAttended() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();

        when(attendanceRepository.existsByStudentIdAndTestId(studentId, testId)).thenReturn(true);

        // When - Then
        assertDoesNotThrow(() -> testAttendanceService.checkStudentAttendedTest(studentId, testId));
    }

    @Test
    void checkStudentAttendedTest_shouldThrowIfNotAttended() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();

        when(attendanceRepository.existsByStudentIdAndTestId(studentId, testId)).thenReturn(false);

        // When - Then
        assertThrows(ValidationException.class,
                () -> testAttendanceService.checkStudentAttendedTest(studentId, testId));
    }

    @Test
    void getAttendedTestIds_shouldReturnTestIdList() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID testId1 = UUID.randomUUID();
        UUID testId2 = UUID.randomUUID();

        List<TestAttendance> attendances = List.of(
                TestAttendance.builder().testId(testId1).build(),
                TestAttendance.builder().testId(testId2).build()
        );

        when(attendanceRepository.findAllByStudentId(studentId)).thenReturn(attendances);

        // When
        List<UUID> result = testAttendanceService.getAttendedTestIds(studentId);

        // Then
        assertEquals(List.of(testId1, testId2), result);
    }
}
