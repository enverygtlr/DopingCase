package com.enverygtlr.dopingcase.mapper;

import com.enverygtlr.dopingcase.domain.entity.Test;
import com.enverygtlr.dopingcase.domain.request.TestRequest;
import com.enverygtlr.dopingcase.domain.response.QuestionResponse;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TestMapper {

    Test toEntity(TestRequest request);

    @Mapping(source = "test.id", target = "testId")
    @Mapping(source = "test.title", target = "title")
    @Mapping(source = "questions", target = "questions")
    TestResponse toResponse(Test test, List<QuestionResponse> questions);
}
