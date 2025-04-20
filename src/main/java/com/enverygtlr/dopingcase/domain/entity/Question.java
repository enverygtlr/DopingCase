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
@Table(name = "question")
public class Question extends BaseEntity {

    @Column(name = "test_id")
    private UUID testId;

    @Column(nullable = false)
    private String content;

}
