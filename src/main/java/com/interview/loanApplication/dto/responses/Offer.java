package com.interview.loanApplication.dto.responses;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Offer {

    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emi;
    private BigDecimal totalPayable;
}