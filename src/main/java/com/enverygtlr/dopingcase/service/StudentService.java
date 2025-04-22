package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Student;
import com.enverygtlr.dopingcase.domain.request.StudentRequest;
import com.enverygtlr.dopingcase.domain.response.StudentResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.mapper.StudentMapper;
import com.enverygtlr.dopingcase.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentResponse getStudent(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> NotFoundException.forStudent(studentId.toString()));

        return studentMapper.toResponse(student);
    }

    public List<StudentResponse> getStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        Student student = studentMapper.toEntity(request);
        Student saved = studentRepository.save(student);
        return studentMapper.toResponse(saved);
    }

    @Transactional
    public StudentResponse updateStudent(UUID studentId, StudentRequest request) {
        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> NotFoundException.forStudent(studentId.toString()));

        existing.setStudentNo(request.studentNo());
        existing.setEmail(request.email());
        existing.setFirstName(request.firstName());
        existing.setLastName(request.lastName());

        Student updated = studentRepository.save(existing);
        return studentMapper.toResponse(updated);
    }

    @Transactional
    public void deleteStudent(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw NotFoundException.forStudent(studentId.toString());
        }
        studentRepository.deleteById(studentId);
    }

    public void checkStudentExists(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw NotFoundException.forStudent(studentId.toString());
        }
    }

}
