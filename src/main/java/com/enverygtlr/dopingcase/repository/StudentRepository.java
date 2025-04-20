package com.enverygtlr.dopingcase.repository;

import com.enverygtlr.dopingcase.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
