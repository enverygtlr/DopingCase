package com.enverygtlr.dopingcase.controller;

import com.enverygtlr.dopingcase.domain.request.StudentAnswerRequest;
import com.enverygtlr.dopingcase.service.StudentAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor
public class StudentAnswerController {

    private final StudentAnswerService studentAnswerService;

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswer(@RequestBody @Valid StudentAnswerRequest request) {
        studentAnswerService.submitAnswer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/change")
    public ResponseEntity<Void> changeAnswer(@RequestBody @Valid StudentAnswerRequest request) {
        studentAnswerService.changeAnswer(request);
        return ResponseEntity.noContent().build();
    }
}