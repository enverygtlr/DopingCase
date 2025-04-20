package com.enverygtlr.dopingcase.repository;

import com.enverygtlr.dopingcase.domain.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface ChoiceRepository extends JpaRepository<Choice, UUID> {

    List<Choice> findAllByQuestionId(UUID questionId);

    void deleteAllByQuestionId(UUID questionId);
}