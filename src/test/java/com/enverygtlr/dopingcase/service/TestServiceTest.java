package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Test;
import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.domain.request.TestRequest;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import com.enverygtlr.dopingcase.domain.response.TestMetaInfo;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.mapper.TestMapper;
import com.enverygtlr.dopingcase.repository.TestRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private TestMapper testMapper;

    @InjectMocks
    private TestService testService;

    @org.junit.jupiter.api.Test
    void createTest_shouldCreateTestAndReturnResponse() {
        // Given
        UUID testId = UUID.randomUUID();
        QuestionRequest q1 = new QuestionRequest("Q1", List.of());
        QuestionResponse r1 = mock(QuestionResponse.class);
        TestRequest request = new TestRequest("Test Title", List.of(q1));

        Test testEntity = new Test();
        Test savedTest = new Test();
        savedTest.setId(testId);

        TestResponse expected = mock(TestResponse.class);

        when(testMapper.toEntity(request)).thenReturn(testEntity);
        when(testRepository.save(testEntity)).thenReturn(savedTest);
        when(questionService.createQuestion(testId, q1)).thenReturn(r1);
        when(testMapper.toResponse(savedTest, List.of(r1))).thenReturn(expected);

        // When
        TestResponse result = testService.createTest(request);

        // Then
        assertEquals(expected, result);
        verify(questionService).createQuestion(testId, q1);
    }

    @org.junit.jupiter.api.Test
    void getAllTestMetaInfos_shouldReturnMappedMetaInfoList() {
        // Given
        UUID testId = UUID.randomUUID();
        Test test = new Test();
        test.setId(testId);
        test.setTitle("Test 1");

        when(testRepository.findAll()).thenReturn(List.of(test));

        // When
        List<TestMetaInfo> result = testService.getAllTestMetaInfos();

        // Then
        assertEquals(1, result.size());
        assertEquals(testId, result.getFirst().testId());
        assertEquals("Test 1", result.getFirst().title());
    }

    @org.junit.jupiter.api.Test
    void getAllTests_shouldReturnPagedResponses() {
        // Given
        UUID testId = UUID.randomUUID();
        Test test = new Test();
        test.setId(testId);
        QuestionResponse qr = mock(QuestionResponse.class);
        TestResponse tr = mock(TestResponse.class);

        Page<Test> page = new PageImpl<>(List.of(test));
        Pageable pageable = PageRequest.of(0, 10);

        when(testRepository.findAll(pageable)).thenReturn(page);
        when(questionService.getQuestionsByTestId(testId)).thenReturn(List.of(qr));
        when(testMapper.toResponse(test, List.of(qr))).thenReturn(tr);

        // When
        Page<TestResponse> result = testService.getAllTests(pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(tr, result.getContent().getFirst());
    }

    @org.junit.jupiter.api.Test
    void getTestById_shouldReturnTestResponse() {
        // Given
        UUID testId = UUID.randomUUID();
        Test test = new Test();
        List<QuestionResponse> questionResponses = List.of(mock(QuestionResponse.class));
        TestResponse expected = mock(TestResponse.class);

        when(testRepository.findById(testId)).thenReturn(Optional.of(test));
        when(questionService.getQuestionsByTestId(testId)).thenReturn(questionResponses);
        when(testMapper.toResponse(test, questionResponses)).thenReturn(expected);

        // When
        TestResponse result = testService.getTestById(testId);

        // Then
        assertEquals(expected, result);
    }

    @org.junit.jupiter.api.Test
    void getTestById_shouldThrowIfNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> testService.getTestById(testId));
    }

    @org.junit.jupiter.api.Test
    void updateTest_shouldUpdateTestAndQuestions() {
        // Given
        UUID testId = UUID.randomUUID();
        QuestionRequest q1 = new QuestionRequest("Updated Q", List.of());
        QuestionResponse qResponse = mock(QuestionResponse.class);
        TestRequest request = new TestRequest("Updated Test", List.of(q1));
        Test test = new Test();
        TestResponse expected = mock(TestResponse.class);

        when(testRepository.findById(testId)).thenReturn(Optional.of(test));
        when(testRepository.save(test)).thenReturn(test);
        when(questionService.createQuestion(testId, q1)).thenReturn(qResponse);
        when(testMapper.toResponse(test, List.of(qResponse))).thenReturn(expected);

        // When
        TestResponse result = testService.updateTest(testId, request);

        // Then
        assertEquals("Updated Test", test.getTitle());
        assertEquals(expected, result);
        verify(questionService).deleteQuestionsByTestId(testId);
    }

    @org.junit.jupiter.api.Test
    void updateTest_shouldThrowIfNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        TestRequest request = new TestRequest("New Title", List.of());

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> testService.updateTest(testId, request));
    }

    @org.junit.jupiter.api.Test
    void deleteTest_shouldDeleteTestAndQuestions() {
        // Given
        UUID testId = UUID.randomUUID();

        // When
        testService.deleteTest(testId);

        // Then
        verify(questionService).deleteQuestionsByTestId(testId);
        verify(testRepository).deleteById(testId);
    }

    @org.junit.jupiter.api.Test
    void checkTestExists_shouldDoNothingIfExists() {
        // Given
        UUID testId = UUID.randomUUID();
        when(testRepository.existsById(testId)).thenReturn(true);

        // When - Then
        assertDoesNotThrow(() -> testService.checkTestExists(testId));
    }

    @org.junit.jupiter.api.Test
    void checkTestExists_shouldThrowIfNotExists() {
        // Given
        UUID testId = UUID.randomUUID();
        when(testRepository.existsById(testId)).thenReturn(false);

        // When - Then
        assertThrows(NotFoundException.class, () -> testService.checkTestExists(testId));
    }
}

