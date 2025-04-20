package com.enverygtlr.dopingcase.controller;

import com.enverygtlr.dopingcase.domain.entity.Student;
import com.enverygtlr.dopingcase.domain.request.StudentRequest;
import com.enverygtlr.dopingcase.domain.response.StudentResponse;
import com.enverygtlr.dopingcase.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentRequest student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable UUID id,
                                                         @RequestBody @Valid StudentRequest student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

}
