package com.enverygtlr.dopingcase.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "student_answer")
public class StudentAnswer extends BaseEntity {

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "choice_id", nullable = false)
    private UUID choiceId;
}
