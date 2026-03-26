package com.interview.loanApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.loanApplication.dto.request.LoanApplicationRequest;
import com.interview.loanApplication.dto.responses.LoanApplicationResponse;
import com.interview.loanApplication.service.LoanApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService service;

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> create(
            @Valid @RequestBody LoanApplicationRequest request) {

        return ResponseEntity.ok(service.checkApplication(request));
    }
}