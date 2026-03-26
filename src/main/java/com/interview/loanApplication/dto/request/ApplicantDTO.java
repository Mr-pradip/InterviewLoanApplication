package com.interview.loanApplication.dto.request;

import java.math.BigDecimal;

import com.interview.loanApplication.enums.EmploymentType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicantDTO {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 21, message = "Minimum age is 21")
    @Max(value = 60, message = "Maximum age is 60")
    private Integer age;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "0.01", message = "Income must be greater than 0")
    private BigDecimal monthlyIncome;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Minimum credit score is 300")
    @Max(value = 900, message = "Maximum credit score is 900")
    private Integer creditScore;
}