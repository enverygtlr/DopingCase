package com.enverygtlr.dopingcase.mapper;

import com.enverygtlr.dopingcase.domain.entity.Choice;
import com.enverygtlr.dopingcase.domain.request.ChoiceRequest;
import com.enverygtlr.dopingcase.domain.response.ChoiceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ChoiceMapper {

    @Mapping(target = "choiceId", source = "id")
    ChoiceResponse toResponse(Choice choice);

    default List<Choice> toChoiceEntities(List<ChoiceRequest> requests, UUID questionId) {
        return requests.stream()
                .map(r -> Choice.builder()
                        .questionId(questionId)
                        .content(r.content())
                        .isCorrectChoice(r.isCorrectChoice())
                        .build())
                .toList();
    }

    List<ChoiceResponse> toResponseList(List<Choice> choices);
}
