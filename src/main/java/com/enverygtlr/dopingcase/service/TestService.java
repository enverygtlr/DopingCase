package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Test;
import com.enverygtlr.dopingcase.domain.request.TestRequest;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.mapper.TestMapper;
import com.enverygtlr.dopingcase.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final QuestionService questionService;
    private final TestMapper testMapper;

    @Transactional
    public TestResponse createTest(TestRequest request) {
        Test test = testMapper.toEntity(request);
        test = testRepository.save(test);

        UUID testId = test.getId();

        List<QuestionResponse> questionResponses = request.questions().stream()
                .map(question -> questionService.createQuestion(testId, question))
                .toList();

        TestResponse response = testMapper.toResponse(test, questionResponses);
        return response;
    }

    public List<TestResponse> getAllTests() {
        return testRepository.findAll().stream()
                .map(test -> {
                    var questions = questionService.getQuestionsByTestId(test.getId());
                    return testMapper.toResponse(test, questions);
                })
                .toList();
    }

    public TestResponse getTestById(UUID testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(NotFoundException::new);

        List<QuestionResponse> questionResponses = questionService.getQuestionsByTestId(testId);
        return testMapper.toResponse(test, questionResponses);
    }

    @Transactional
    public TestResponse updateTest(UUID testId, TestRequest request) {
        Test test = testRepository.findById(testId)
                .orElseThrow(NotFoundException::new);

        test.setTitle(request.title());
        testRepository.save(test);

        questionService.deleteQuestionsByTestId(testId);

        List<QuestionResponse> updatedQuestions = request.questions().stream()
                .map(q -> questionService.createQuestion(testId, q))
                .toList();

        return testMapper.toResponse(test, updatedQuestions);
    }

    @Transactional
    public void deleteTest(UUID testId) {
        questionService.deleteQuestionsByTestId(testId);
        testRepository.deleteById(testId);
    }

    public void checkTestExists(UUID testId) {
        if (!testRepository.existsById(testId)) {
            throw new NotFoundException();
        }
    }

}
