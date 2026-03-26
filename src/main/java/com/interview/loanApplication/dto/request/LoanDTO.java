package com.interview.loanApplication.dto.request;

import java.math.BigDecimal;

import com.interview.loanApplication.enums.LoanPurpose;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanDTO {

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "10000.00", message = "Minimum loan amount is 10,000")
    @DecimalMax(value = "5000000.00", message = "Maximum loan amount is 50,00,000")
    private BigDecimal amount;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Minimum tenure is 6 months")
    @Max(value = 360, message = "Maximum tenure is 360 months")
    private Integer tenureMonths;

    @NotNull(message = "Loan purpose is required")
    private LoanPurpose purpose;
}