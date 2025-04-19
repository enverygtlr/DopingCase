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
@Table(name = "question")
public class Question extends BaseEntity {

    @Column(name = "test_id")
    private Long testId;

    @Column(nullable = false)
    private String content;

}
