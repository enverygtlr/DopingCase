package com.enverygtlr.dopingcase.repository;

import com.enverygtlr.dopingcase.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findAllByTestId(UUID testId);
}
