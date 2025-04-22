package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Question;
import com.enverygtlr.dopingcase.domain.entity.Test;
import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.domain.request.QuestionUpdateRequest;
import com.enverygtlr.dopingcase.domain.response.ChoiceResponse;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.mapper.QuestionMapper;
import com.enverygtlr.dopingcase.repository.ChoiceRepository;
import com.enverygtlr.dopingcase.repository.QuestionRepository;
import com.enverygtlr.dopingcase.repository.TestRepository;
import com.enverygtlr.dopingcase.validator.QuestionValidator;
import com.enverygtlr.dopingcase.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuestionValidator questionValidator;
    private final TestRepository testRepository;
    private final ChoiceService choiceService;

    @Transactional
    public QuestionResponse createQuestion(UUID testId, QuestionRequest request) {
        if(!testRepository.existsById(testId)) {
            NotFoundException.forTest(testId.toString());
        };

        questionValidator.validate(new QuestionValidator.Context(testId, request));

        Question question = questionMapper.toEntity(request);
        question.setTestId(testId);
        question = questionRepository.save(question);

        List<ChoiceResponse> choices = choiceService.createChoices(question.getId(), request.choices());

        return questionMapper.toResponse(question, choices);
    }

    public QuestionResponse getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(questionId.toString()));

        List<ChoiceResponse> choices = choiceService.getChoicesByQuestionId(questionId);

        return questionMapper.toResponse(question, choices);
    }

    @Transactional
    public QuestionResponse updateQuestion(UUID questionId, QuestionUpdateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(questionId.toString()));

        if(!testRepository.existsById(request.testId())) {
            throw NotFoundException.forTest(request.testId().toString());
        };

        question.setTestId(request.testId());
        question.setContent(request.content());
        questionRepository.save(question);

        choiceService.deleteChoicesByQuestionId(questionId);
        List<ChoiceResponse> updatedChoices = choiceService.createChoices(questionId, request.choices());

        return questionMapper.toResponse(question, updatedChoices);
    }

    @Transactional
    public void deleteQuestion(UUID questionId) {
        choiceService.deleteChoicesByQuestionId(questionId);
        questionRepository.deleteById(questionId);
    }

    public List<QuestionResponse> getQuestionsByTestId(UUID testId) {
        return questionRepository.findAllByTestId(testId).stream()
                .map(q -> {
                    var choices = choiceService.getChoicesByQuestionId(q.getId());
                    return questionMapper.toResponse(q, choices);
                })
                .toList();
    }

    @Transactional
    public void deleteQuestionsByTestId(UUID testId) {
        List<Question> questions = questionRepository.findAllByTestId(testId);
        for (Question q : questions) {
            deleteQuestion(q.getId());
        }
    }

    public void checkQuestionBelongsToTest(UUID questionId, UUID testId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(questionId.toString()));

        if (!question.getTestId().equals(testId)) {
            throw ValidationException.questionDoesNotBelong(questionId.toString(), testId.toString());
        }
    }
}