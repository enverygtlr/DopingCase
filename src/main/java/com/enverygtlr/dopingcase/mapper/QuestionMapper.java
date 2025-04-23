package com.enverygtlr.dopingcase.mapper;


import com.enverygtlr.dopingcase.domain.entity.Question;
import com.enverygtlr.dopingcase.domain.request.QuestionRequest;
import com.enverygtlr.dopingcase.domain.response.ChoiceResponse;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface QuestionMapper {

    Question toEntity(QuestionRequest request);

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.testId", target = "testId")
    @Mapping(source = "question.content", target = "content")
    @Mapping(source = "choices", target = "choices")
    QuestionResponse toResponse(Question question, List<ChoiceResponse> choices);
}