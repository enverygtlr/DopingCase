package com.enverygtlr.dopingcase.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "choice")
public class Choice extends BaseEntity {

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column
    private String content;

    @Column(name = "correct_choice", nullable = false)
    private Boolean isCorrectChoice;

}
