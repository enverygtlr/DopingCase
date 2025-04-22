package com.enverygtlr.dopingcase.controller;


import com.enverygtlr.dopingcase.domain.request.TestAttendanceRequest;
import com.enverygtlr.dopingcase.domain.request.TestRequest;
import com.enverygtlr.dopingcase.domain.response.TestMetaInfo;
import com.enverygtlr.dopingcase.domain.response.TestResponse;
import com.enverygtlr.dopingcase.service.TestAttendanceService;
import com.enverygtlr.dopingcase.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final TestAttendanceService testAttendanceService;

    @PostMapping
    public ResponseEntity<TestResponse> createTest(@RequestBody @Valid TestRequest request) {
        TestResponse response = testService.createTest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TestResponse>> getAllTestsPaged(Pageable pageable) {
        return ResponseEntity.ok(testService.getAllTests(pageable));
    }

    @GetMapping("/meta")
    public ResponseEntity<List<TestMetaInfo>> getAllTestMetaInfo() {
        return ResponseEntity.ok(testService.getAllTestMetaInfos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResponse> getTest(@PathVariable UUID id) {
        return ResponseEntity.ok(testService.getTestById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestResponse> updateTest(@PathVariable UUID id,
                                                   @RequestBody @Valid TestRequest request) {
        return ResponseEntity.ok(testService.updateTest(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable UUID id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attend")
    public ResponseEntity<Void> attendTest(@RequestBody @Valid TestAttendanceRequest request) {
        testAttendanceService.attendTest(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/attend")
    public ResponseEntity<Void> deleteAttendance(@RequestBody @Valid TestAttendanceRequest request) {
        testAttendanceService.removeAttendance(request.studentId(), request.testId());
        return ResponseEntity.ok().build();
    }


}