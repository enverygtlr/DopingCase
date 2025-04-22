package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.TestAttendance;
import com.enverygtlr.dopingcase.domain.request.TestAttendanceRequest;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.repository.TestAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestAttendanceService {
    private final TestAttendanceRepository attendanceRepository;
    private final StudentService studentService;
    private final TestService testService;

    @Transactional
    public void attendTest(TestAttendanceRequest testAttendanceRequest) {
        UUID studentId = testAttendanceRequest.studentId();
        UUID testId = testAttendanceRequest.testId();

        testService.checkTestExists(testId);
        studentService.checkStudentExists(studentId);

        if (attendanceRepository.existsByStudentIdAndTestId(studentId, testId)) {
            throw ValidationException.studentAlreadyAttended(studentId.toString(), testId.toString());
        }

        TestAttendance attendance = TestAttendance.builder()
                .studentId(studentId)
                .testId(testId)
                .build();

        attendanceRepository.save(attendance);
    }

    @Transactional
    public void removeAttendance(UUID testId, UUID studentId) {
        testService.checkTestExists(testId);
        studentService.checkStudentExists(studentId);

        TestAttendance attendance = attendanceRepository.findByStudentIdAndTestId(studentId, testId)
                .orElseThrow(() -> new NotFoundException(testId.toString()));

        attendanceRepository.delete(attendance);
    }

    public void checkStudentAttendedTest(UUID studentId, UUID testId) {
        if (!attendanceRepository.existsByStudentIdAndTestId(studentId, testId)) {
            throw ValidationException.testNotAttended(studentId.toString(), testId.toString());
        }
    }

    public List<UUID> getAttendedTestIds(UUID studentId) {
        return attendanceRepository.findAllByStudentId(studentId).stream()
                .map(TestAttendance::getTestId)
                .toList();
    }

}
