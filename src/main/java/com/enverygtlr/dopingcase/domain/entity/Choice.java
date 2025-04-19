package com.enverygtlr.dopingcase.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "choice")
public class Choice extends BaseEntity {

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column
    private String content;

    @Column(name = "correct_choice", nullable = false)
    private Boolean isCorrectChoice;

}
