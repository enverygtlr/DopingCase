package com.enverygtlr.dopingcase.repository;

import com.enverygtlr.dopingcase.domain.entity.TestAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TestAttendanceRepository extends JpaRepository<TestAttendance, UUID> {
    boolean existsByStudentIdAndTestId(UUID studentId, UUID testId);
    Optional<TestAttendance> findByStudentIdAndTestId(UUID studentId, UUID testId);
    List<TestAttendance> findAllByTestId(UUID testId);
    List<TestAttendance> findAllByStudentId(UUID studentId);
}
