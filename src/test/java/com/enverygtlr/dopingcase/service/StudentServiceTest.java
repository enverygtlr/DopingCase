package com.enverygtlr.dopingcase.service;

import com.enverygtlr.dopingcase.domain.entity.Student;
import com.enverygtlr.dopingcase.domain.request.StudentRequest;
import com.enverygtlr.dopingcase.domain.response.StudentResponse;
import com.enverygtlr.dopingcase.exception.NotFoundException;
import com.enverygtlr.dopingcase.mapper.StudentMapper;
import com.enverygtlr.dopingcase.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getStudent_shouldReturnStudentResponse() {
        // Given
        UUID studentId = UUID.randomUUID();
        Student student = new Student();
        StudentResponse expected = mock(StudentResponse.class);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toResponse(student)).thenReturn(expected);

        // When
        StudentResponse result = studentService.getStudent(studentId);

        // Then
        assertEquals(expected, result);
    }

    @Test
    void getStudent_shouldThrowWhenNotFound() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> studentService.getStudent(studentId));
    }

    @Test
    void getStudents_shouldReturnAllMappedResponses() {
        // Given
        Student student1 = new Student();
        Student student2 = new Student();

        StudentResponse response1 = mock(StudentResponse.class);
        StudentResponse response2 = mock(StudentResponse.class);

        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));
        when(studentMapper.toResponse(student1)).thenReturn(response1);
        when(studentMapper.toResponse(student2)).thenReturn(response2);

        // When
        List<StudentResponse> result = studentService.getStudents();

        // Then
        assertEquals(List.of(response1, response2), result);
    }

    @Test
    void createStudent_shouldSaveAndReturnResponse() {
        // Given
        StudentRequest request = new StudentRequest("John", "Doe", "john@example.com", 100L);
        Student student = new Student();
        Student saved = new Student();
        StudentResponse expected = mock(StudentResponse.class);

        when(studentMapper.toEntity(request)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(saved);
        when(studentMapper.toResponse(saved)).thenReturn(expected);

        // When
        StudentResponse result = studentService.createStudent(request);

        // Then
        assertEquals(expected, result);
        verify(studentRepository).save(student);
    }

    @Test
    void updateStudent_shouldUpdateAndReturnResponse() {
        // Given
        UUID studentId = UUID.randomUUID();
        StudentRequest request = new StudentRequest("Jane", "Smith", "jane@example.com", 1234L);

        Student existing = new Student();
        Student updated = new Student();
        StudentResponse expected = mock(StudentResponse.class);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(updated);
        when(studentMapper.toResponse(updated)).thenReturn(expected);

        // When
        StudentResponse result = studentService.updateStudent(studentId, request);

        // Then
        assertEquals(expected, result);
        assertEquals("Jane", existing.getFirstName());
        assertEquals("Smith", existing.getLastName());
        assertEquals("jane@example.com", existing.getEmail());
        assertEquals(1234L, existing.getStudentNo());
    }

    @Test
    void updateStudent_shouldThrowWhenStudentNotFound() {
        // Given
        UUID studentId = UUID.randomUUID();
        StudentRequest request = mock(StudentRequest.class);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NotFoundException.class, () -> studentService.updateStudent(studentId, request));
    }

    @Test
    void deleteStudent_shouldCallDeleteIfExists() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.existsById(studentId)).thenReturn(true);

        // When
        studentService.deleteStudent(studentId);

        // Then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void deleteStudent_shouldThrowWhenNotExists() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.existsById(studentId)).thenReturn(false);

        // When - Then
        assertThrows(NotFoundException.class, () -> studentService.deleteStudent(studentId));
    }

    @Test
    void checkStudentExists_shouldDoNothingIfExists() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.existsById(studentId)).thenReturn(true);

        // When - Then
        assertDoesNotThrow(() -> studentService.checkStudentExists(studentId));
    }

    @Test
    void checkStudentExists_shouldThrowIfNotExists() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.existsById(studentId)).thenReturn(false);

        // When - Then
        assertThrows(NotFoundException.class, () -> studentService.checkStudentExists(studentId));
    }
}
