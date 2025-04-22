package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Question;
import com.enverygtlr.dopingcase.domain.request.ChoiceRequest;
import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.domain.request.QuestionUpdateRequest;
import com.enverygtlr.dopingcase.domain.response.ChoiceResponse;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.mapper.QuestionMapper;
import com.enverygtlr.dopingcase.repository.QuestionRepository;
import com.enverygtlr.dopingcase.repository.TestRepository;
import com.enverygtlr.dopingcase.validator.QuestionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private QuestionValidator questionValidator;

    @Mock
    private TestRepository testRepository;

    @Mock
    private ChoiceService choiceService;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void createQuestion_shouldSucceed() {
        // Given
        UUID testId = UUID.randomUUID();
        QuestionRequest request = new QuestionRequest("Question content", List.of(
                new ChoiceRequest("A", true), new ChoiceRequest("B", false)
        ));

        Question question = new Question();
        Question savedQuestion = new Question();
        savedQuestion.setId(UUID.randomUUID());
        savedQuestion.setTestId(testId);

        List<ChoiceResponse> choices = List.of(
                new ChoiceResponse(UUID.randomUUID(), savedQuestion.getId(), "A", true),
                new ChoiceResponse(UUID.randomUUID(), savedQuestion.getId(), "B", false)
        );

        QuestionResponse expectedResponse = new QuestionResponse(
                savedQuestion.getId(), testId, "Question content", choices
        );

        when(testRepository.existsById(testId)).thenReturn(true);
        when(questionMapper.toEntity(request)).thenReturn(question);
        when(questionRepository.save(question)).thenReturn(savedQuestion);
        when(choiceService.createChoices(savedQuestion.getId(), request.choices())).thenReturn(choices);
        when(questionMapper.toResponse(savedQuestion, choices)).thenReturn(expectedResponse);

        // When
        QuestionResponse result = questionService.createQuestion(testId, request);

        // Then
        assertEquals(expectedResponse, result);
        verify(questionValidator).validate(any());
    }

    @Test
    void createQuestion_shouldThrowWhenTestNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        QuestionRequest request = mock(QuestionRequest.class);

        when(testRepository.existsById(testId)).thenReturn(false);

        // When - Then
        assertThrows(NotFoundException.class, () -> questionService.createQuestion(testId, request));
    }

    @Test
    void getQuestionById_shouldReturnQuestion() {
        // Given
        UUID questionId = UUID.randomUUID();
        Question question = new Question();
        List<ChoiceResponse> choices = List.of(mock(ChoiceResponse.class));
        QuestionResponse expected = mock(QuestionResponse.class);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(choiceService.getChoicesByQuestionId(questionId)).thenReturn(choices);
        when(questionMapper.toResponse(question, choices)).thenReturn(expected);

        // When
        QuestionResponse result = questionService.getQuestionById(questionId);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void getQuestionById_shouldThrowIfNotFound() {
        // Given
        UUID questionId = UUID.randomUUID();
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> questionService.getQuestionById(questionId));
    }

    @Test
    void updateQuestion_shouldSucceed() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        QuestionUpdateRequest request = new QuestionUpdateRequest(questionId, testId, "Updated", List.of(
                new ChoiceRequest("A", true)
        ));

        Question question = new Question();
        question.setId(questionId);
        QuestionResponse expected = mock(QuestionResponse.class);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(testRepository.existsById(testId)).thenReturn(true);
        when(choiceService.createChoices(questionId, request.choices())).thenReturn(List.of());
        when(questionMapper.toResponse(question, List.of())).thenReturn(expected);

        // When
        QuestionResponse result = questionService.updateQuestion(questionId, request);

        // Then
        assertEquals(expected, result);
        verify(choiceService).deleteChoicesByQuestionId(questionId);
    }

    @Test
    void updateQuestion_shouldThrowIfQuestionNotFound() {
        // Given
        UUID questionId = UUID.randomUUID();
        QuestionUpdateRequest request = mock(QuestionUpdateRequest.class);

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> questionService.updateQuestion(questionId, request));
    }

    @Test
    void updateQuestion_shouldThrowIfTestNotFound() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        Question question = new Question();
        QuestionUpdateRequest request = new QuestionUpdateRequest(questionId, testId, "Q", List.of());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(testRepository.existsById(testId)).thenReturn(false);

        // When - Then
        assertThrows(NotFoundException.class, () -> questionService.updateQuestion(questionId, request));
    }

    @Test
    void deleteQuestion_shouldCallDeleteMethods() {
        // Given
        UUID questionId = UUID.randomUUID();

        // When
        questionService.deleteQuestion(questionId);

        // Then
        verify(choiceService).deleteChoicesByQuestionId(questionId);
        verify(questionRepository).deleteById(questionId);
    }

    @Test
    void getQuestionsByTestId_shouldReturnList() {
        // Given
        UUID testId = UUID.randomUUID();
        Question q1 = new Question();
        q1.setId(UUID.randomUUID());

        List<ChoiceResponse> choices = List.of(mock(ChoiceResponse.class));
        QuestionResponse r1 = mock(QuestionResponse.class);

        when(questionRepository.findAllByTestId(testId)).thenReturn(List.of(q1));
        when(choiceService.getChoicesByQuestionId(q1.getId())).thenReturn(choices);
        when(questionMapper.toResponse(q1, choices)).thenReturn(r1);

        // When
        List<QuestionResponse> result = questionService.getQuestionsByTestId(testId);

        // Then
        assertEquals(List.of(r1), result);
    }

    @Test
    void deleteQuestionsByTestId_shouldDeleteAll() {
        // Given
        UUID testId = UUID.randomUUID();
        Question q1 = new Question();
        q1.setId(UUID.randomUUID());
        Question q2 = new Question();
        q2.setId(UUID.randomUUID());

        when(questionRepository.findAllByTestId(testId)).thenReturn(List.of(q1, q2));

        // When
        questionService.deleteQuestionsByTestId(testId);

        // Then
        verify(choiceService, times(2)).deleteChoicesByQuestionId(any());
        verify(questionRepository, times(2)).deleteById(any());
    }

    @Test
    void checkQuestionBelongsToTest_shouldPass() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        Question question = new Question();
        question.setTestId(testId);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // When - Then
        assertDoesNotThrow(() -> questionService.checkQuestionBelongsToTest(questionId, testId));
    }

    @Test
    void checkQuestionBelongsToTest_shouldThrowIfNotBelongs() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        Question question = new Question();
        question.setTestId(UUID.randomUUID());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // When - Then
        assertThrows(ValidationException.class,
                () -> questionService.checkQuestionBelongsToTest(questionId, testId));
    }

    @Test
    void checkQuestionBelongsToTest_shouldThrowIfNotFound() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class,
                () -> questionService.checkQuestionBelongsToTest(questionId, testId));
    }
}
