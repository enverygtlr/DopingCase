package com.enverygtlr.dopingcase.controller;

import com.enverygtlr.dopingcase.domain.response.StudentReportResponse;
import com.enverygtlr.dopingcase.service.StudentReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class StudentReportController {

    private final StudentReportService studentReportService;

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentReportResponse> getStudentReport(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentReportService.getStudentReport(studentId));
    }
}