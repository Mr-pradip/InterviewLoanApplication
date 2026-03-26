package com.interview.loanApplication.dto.responses;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.interview.loanApplication.enums.RiskBand;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanApplicationResponse {

    private UUID applicationId;
    private String status;
    private RiskBand riskBand;
    private Offer offer;
    private List<String> rejectionReasons;
}