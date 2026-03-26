package com.interview.loanApplication.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.interview.loanApplication.dto.request.LoanApplicationRequest;
import com.interview.loanApplication.dto.responses.LoanApplicationResponse;
import com.interview.loanApplication.dto.responses.Offer;
import com.interview.loanApplication.enums.EmploymentType;
import com.interview.loanApplication.enums.RiskBand;
import com.interview.loanApplication.service.LoanApplicationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(12);

    private final Map<UUID, LoanApplicationResponse> store = new HashMap<>();

    @Override
    public LoanApplicationResponse checkApplication(LoanApplicationRequest request) {

        log.info("Starting loan evaluation");
        UUID id = UUID.randomUUID();

        var applicant = request.getApplicant();
        var loan = request.getLoan();

        log.debug("Applicant Details: {}", applicant);
        log.debug("Loan Details: {}", loan);
        
        List<String> reasons = new ArrayList<>();

        RiskBand riskBand = getRiskBand(applicant.getCreditScore());
        BigDecimal rate = calculateRate(riskBand, applicant.getEmploymentType(), loan.getAmount());
        BigDecimal emi = calculateEMI(loan.getAmount(), rate, loan.getTenureMonths());

        if (applicant.getCreditScore() < 600)
            reasons.add("LOW_CREDIT_SCORE");

        if (applicant.getAge() + (loan.getTenureMonths() / 12.0) > 65)
            reasons.add("AGE_TENURE_LIMIT_EXCEEDED");

        if (emi.compareTo(applicant.getMonthlyIncome().multiply(BigDecimal.valueOf(0.6))) > 0)
            reasons.add("EMI_EXCEEDS_60_PERCENT");

        if (emi.compareTo(applicant.getMonthlyIncome().multiply(BigDecimal.valueOf(0.5))) > 0)
            reasons.add("EMI_EXCEEDS_50_PERCENT");

        LoanApplicationResponse response;

        if (!reasons.isEmpty()) {
            log.info("Loan rejected due to: {}", reasons);
            response = LoanApplicationResponse.builder()
                    .applicationId(id)
                    .status("REJECTED")
                    .rejectionReasons(reasons)
                    .build();
        } else {
            log.info("Loan approved for amount: {}", loan.getAmount());
            BigDecimal total = emi.multiply(BigDecimal.valueOf(loan.getTenureMonths()));

            response = LoanApplicationResponse.builder()
                    .applicationId(id)
                    .status("APPROVED")
                    .riskBand(riskBand)
                    .offer(Offer.builder()
                            .interestRate(rate)
                            .tenureMonths(loan.getTenureMonths())
                            .emi(emi)
                            .totalPayable(total)
                            .build())
                    .build();
        }

        store.put(id, response);
        log.info("Loan evaluation completed");
        return response;
    }

    private RiskBand getRiskBand(int score) {
        if (score >= 750) return RiskBand.LOW;
        if (score >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }

    private BigDecimal calculateRate(RiskBand band, EmploymentType type, BigDecimal amount) {
        BigDecimal rate = BASE_RATE;

        if (band == RiskBand.MEDIUM) rate = rate.add(BigDecimal.valueOf(1.5));
        if (band == RiskBand.HIGH) rate = rate.add(BigDecimal.valueOf(3));

        if (type == EmploymentType.SELF_EMPLOYED)
            rate = rate.add(BigDecimal.valueOf(1));

        if (amount.compareTo(BigDecimal.valueOf(1000000)) > 0)
            rate = rate.add(BigDecimal.valueOf(0.5));

        return rate;
    }

    private BigDecimal calculateEMI(BigDecimal p, BigDecimal rate, int n) {
        BigDecimal r = rate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        BigDecimal pow = (r.add(BigDecimal.ONE)).pow(n);

        return p.multiply(r).multiply(pow)
                .divide(pow.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
    }
}