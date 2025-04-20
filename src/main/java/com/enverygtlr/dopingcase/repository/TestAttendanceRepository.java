package com.enverygtlr.dopingcase.repository;

import com.enverygtlr.dopingcase.domain.entity.TestAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestAttendanceRepository extends JpaRepository<TestAttendance, UUID> {
}
