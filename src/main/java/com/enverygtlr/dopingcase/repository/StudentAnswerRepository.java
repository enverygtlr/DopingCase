package com.enverygtlr.dopingcase.repository;

import com.enverygtlr.dopingcase.domain.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, UUID> {
    boolean existsByStudentIdAndQuestionId(UUID studentId, UUID questionId);
    Optional<StudentAnswer> findByStudentIdAndQuestionId(UUID studentId, UUID questionId);
}
