package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.config.CacheNames;
import com.enverygtlr.dopingcase.domain.entity.Test;
import com.enverygtlr.dopingcase.domain.request.TestRequest;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import com.enverygtlr.dopingcase.domain.response.TestMetaInfo;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.mapper.TestMapper;
import com.enverygtlr.dopingcase.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public List<TestMetaInfo> getAllTestMetaInfos() {
        return testRepository.findAll().stream()
                .map(test -> new TestMetaInfo(test.getId(), test.getTitle()))
                .toList();
    }

    public Page<TestResponse> getAllTests(Pageable pageable) {
        return testRepository.findAll(pageable)
                .map(test -> {
                    var questions = questionService.getQuestionsByTestId(test.getId());
                    return testMapper.toResponse(test, questions);
                });
    }

    @Cacheable(cacheNames = CacheNames.TEST_CACHE)
    public TestResponse getTestById(UUID testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> NotFoundException.forTest(testId.toString()));

        List<QuestionResponse> questionResponses = questionService.getQuestionsByTestId(testId);
        return testMapper.toResponse(test, questionResponses);
    }

    @CacheEvict(cacheNames = CacheNames.TEST_CACHE, key = "#testId")
    @Transactional
    public TestResponse updateTest(UUID testId, TestRequest request) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> NotFoundException.forTest(testId.toString()));

        test.setTitle(request.title());
        testRepository.save(test);

        questionService.deleteQuestionsByTestId(testId);

        List<QuestionResponse> updatedQuestions = request.questions().stream()
                .map(q -> questionService.createQuestion(testId, q))
                .toList();

        return testMapper.toResponse(test, updatedQuestions);
    }

    @CacheEvict(cacheNames = CacheNames.TEST_CACHE, key = "#testId")
    @Transactional
    public void deleteTest(UUID testId) {
        questionService.deleteQuestionsByTestId(testId);
        testRepository.deleteById(testId);
    }

    public void checkTestExists(UUID testId) {
        if (!testRepository.existsById(testId)) {
            throw NotFoundException.forTest(testId.toString());
        }
    }

}
