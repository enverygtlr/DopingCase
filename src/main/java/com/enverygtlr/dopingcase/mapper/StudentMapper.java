package com.enverygtlr.dopingcase.mapper;

import com.enverygtlr.dopingcase.domain.entity.Student;
import com.enverygtlr.dopingcase.domain.request.StudentRequest;
import com.enverygtlr.dopingcase.domain.response.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StudentMapper {
    StudentResponse toStudentResponse(Student student);
    Student toEntity(StudentRequest request);
}
