package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Choice;
import com.enverygtlr.dopingcase.domain.request.ChoiceRequest;
import com.enverygtlr.dopingcase.domain.response.ChoiceResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.exception.ValidationException;
import com.enverygtlr.dopingcase.mapper.ChoiceMapper;
import com.enverygtlr.dopingcase.repository.ChoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChoiceService {

    private final ChoiceRepository choiceRepository;
    private final ChoiceMapper choiceMapper;

    @Transactional
    public List<ChoiceResponse> createChoices(UUID questionId, List<ChoiceRequest> requests) {
        List<Choice> choices = choiceMapper.toChoiceEntities(requests, questionId);

        return choiceRepository.saveAll(choices)
                .stream()
                .map(choiceMapper::toResponse)
                .toList();
    }

    public List<ChoiceResponse> getChoicesByQuestionId(UUID questionId) {
        return choiceRepository.findAllByQuestionId(questionId)
                .stream()
                .map(choiceMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteChoicesByQuestionId(UUID questionId) {
        choiceRepository.deleteAllByQuestionId(questionId);
    }

    public void checkChoiceBelongsToQuestion(UUID choiceId, UUID questionId) {
        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new NotFoundException(choiceId.toString()));

        if (!choice.getQuestionId().equals(questionId)) {
            throw ValidationException.choiceDoesNotBelong(choiceId.toString(), questionId.toString());
        }
    }

    public List<Choice> getChoicesByIds(List<UUID> ids) {
        return choiceRepository.findAllById(ids);
    }
}
