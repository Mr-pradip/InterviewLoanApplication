package com.interview.loanApplication.service;

import com.interview.loanApplication.dto.request.LoanApplicationRequest;
import com.interview.loanApplication.dto.responses.LoanApplicationResponse;

public interface LoanApplicationService {
    LoanApplicationResponse checkApplication(LoanApplicationRequest request);
}