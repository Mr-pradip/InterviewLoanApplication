package com.interview.loanApplication.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanApplicationRequest {

    @NotNull(message = "Applicant details are required")
    @Valid
    private ApplicantDTO applicant;

    @NotNull(message = "Loan details are required")
    @Valid
    private LoanDTO loan;
}