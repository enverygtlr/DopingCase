package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Choice;
import com.enverygtlr.dopingcase.domain.request.ChoiceRequest;
import com.enverygtlr.dopingcase.domain.response.ChoiceResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.mapper.ChoiceMapper;
import com.enverygtlr.dopingcase.repository.ChoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChoiceServiceTest {

    @Mock
    private ChoiceRepository choiceRepository;

    @Mock
    private ChoiceMapper choiceMapper;

    @InjectMocks
    private ChoiceService choiceService;

    @Test
    void createChoices_shouldSucceed() {
        // Given
        UUID questionId = UUID.randomUUID();
        List<ChoiceRequest> requests = List.of(
                new ChoiceRequest("A", true),
                new ChoiceRequest("B", false)
        );

        List<Choice> choices = List.of(new Choice(), new Choice());
        List<Choice> savedChoices = List.of(new Choice(), new Choice());

        ChoiceResponse response1 = new ChoiceResponse(UUID.randomUUID(), questionId, "A", true);
        ChoiceResponse response2 = new ChoiceResponse(UUID.randomUUID(), questionId, "B", false);

        when(choiceMapper.toChoiceEntities(requests, questionId)).thenReturn(choices);
        when(choiceRepository.saveAll(choices)).thenReturn(savedChoices);
        when(choiceMapper.toResponse(savedChoices.get(0))).thenReturn(response1);
        when(choiceMapper.toResponse(savedChoices.get(1))).thenReturn(response2);

        // When
        List<ChoiceResponse> result = choiceService.createChoices(questionId, requests);

        // Then
        assertEquals(List.of(response1, response2), result);
        verify(choiceRepository).saveAll(choices);
    }

    @Test
    void getChoicesByQuestionId_shouldReturnResponses() {
        // Given
        UUID questionId = UUID.randomUUID();
        Choice choice1 = new Choice();
        Choice choice2 = new Choice();
        List<Choice> choices = List.of(choice1, choice2);

        ChoiceResponse response1 = new ChoiceResponse(UUID.randomUUID(), questionId, "A", true);
        ChoiceResponse response2 = new ChoiceResponse(UUID.randomUUID(), questionId, "B", false);

        when(choiceRepository.findAllByQuestionId(questionId)).thenReturn(choices);
        when(choiceMapper.toResponse(choice1)).thenReturn(response1);
        when(choiceMapper.toResponse(choice2)).thenReturn(response2);

        // When
        List<ChoiceResponse> result = choiceService.getChoicesByQuestionId(questionId);

        // Then
        assertEquals(List.of(response1, response2), result);
    }

    @Test
    void deleteChoicesByQuestionId_shouldCallRepository() {
        // Given
        UUID questionId = UUID.randomUUID();

        // When
        choiceService.deleteChoicesByQuestionId(questionId);

        // Then
        verify(choiceRepository).deleteAllByQuestionId(questionId);
    }

    @Test
    void checkChoiceBelongsToQuestion_shouldDoNothingIfBelongs() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID choiceId = UUID.randomUUID();
        Choice choice = new Choice();
        choice.setQuestionId(questionId);

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));

        // When - Then
        assertDoesNotThrow(() -> choiceService.checkChoiceBelongsToQuestion(choiceId, questionId));
    }

    @Test
    void checkChoiceBelongsToQuestion_shouldThrowValidationException() {
        // Given
        UUID choiceId = UUID.randomUUID();
        UUID actualQuestionId = UUID.randomUUID();
        UUID givenQuestionId = UUID.randomUUID();

        Choice choice = new Choice();
        choice.setQuestionId(actualQuestionId);

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.of(choice));

        // When - Then
        assertThrows(ValidationException.class,
                () -> choiceService.checkChoiceBelongsToQuestion(choiceId, givenQuestionId));
    }

    @Test
    void checkChoiceBelongsToQuestion_shouldThrowNotFoundException() {
        // Given
        UUID choiceId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();

        when(choiceRepository.findById(choiceId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class,
                () -> choiceService.checkChoiceBelongsToQuestion(choiceId, questionId));
    }

    @Test
    void getChoicesByIds_shouldReturnChoices() {
        // Given
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        List<Choice> expected = List.of(new Choice(), new Choice());

        when(choiceRepository.findAllById(ids)).thenReturn(expected);

        // When
        List<Choice> result = choiceService.getChoicesByIds(ids);

        // Then
        assertEquals(expected, result);
        verify(choiceRepository).findAllById(ids);
    }
}
