package com.interview.loanApplication.service.impl;


import com.interview.loanApplication.dto.request.*;
import com.interview.loanApplication.dto.responses.LoanApplicationResponse;
import com.interview.loanApplication.enums.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationServiceImplTest {

    private LoanApplicationServiceImpl service;

    @BeforeEach
    void setup() {
        service = new LoanApplicationServiceImpl();
    }

    private LoanApplicationRequest validRequest() {
        ApplicantDTO applicant = new ApplicantDTO();
        applicant.setName("Pradip");
        applicant.setAge(30);
        applicant.setMonthlyIncome(BigDecimal.valueOf(80000));
        applicant.setEmploymentType(EmploymentType.SALARIED);
        applicant.setCreditScore(750);

        LoanDTO loan = new LoanDTO();
        loan.setAmount(BigDecimal.valueOf(500000));
        loan.setTenureMonths(36);
        loan.setPurpose(LoanPurpose.PERSONAL);

        LoanApplicationRequest req = new LoanApplicationRequest();
        req.setApplicant(applicant);
        req.setLoan(loan);

        return req;
    }

    // ✅ APPROVED CASE
    @Test
    void shouldApproveLoan() {
        LoanApplicationResponse res = service.checkApplication(validRequest());

        assertEquals("APPROVED", res.getStatus());
        assertNotNull(res.getOffer());
        assertNull(res.getRejectionReasons());
    }

    // ❌ LOW CREDIT SCORE
    @Test
    void shouldRejectLowCreditScore() {
        LoanApplicationRequest req = validRequest();
        req.getApplicant().setCreditScore(500);

        LoanApplicationResponse res = service.checkApplication(req);

        assertEquals("REJECTED", res.getStatus());
        assertTrue(res.getRejectionReasons().contains("LOW_CREDIT_SCORE"));
    }

    // ❌ AGE + TENURE FAIL
    @Test
    void shouldRejectAgeTenureExceeded() {
        LoanApplicationRequest req = validRequest();
        req.getApplicant().setAge(60);
        req.getLoan().setTenureMonths(120);

        LoanApplicationResponse res = service.checkApplication(req);

        assertTrue(res.getRejectionReasons().contains("AGE_TENURE_LIMIT_EXCEEDED"));
    }

    // ❌ EMI > 60%
    @Test
    void shouldRejectEmiAbove60Percent() {
        LoanApplicationRequest req = validRequest();
        req.getApplicant().setMonthlyIncome(BigDecimal.valueOf(10000));

        LoanApplicationResponse res = service.checkApplication(req);

        assertTrue(res.getRejectionReasons().contains("EMI_EXCEEDS_60_PERCENT"));
    }

    // ❌ EMI > 50%
    @Test
    void shouldRejectEmiAbove50Percent() {
        LoanApplicationRequest req = validRequest();
        req.getApplicant().setMonthlyIncome(BigDecimal.valueOf(15000));

        LoanApplicationResponse res = service.checkApplication(req);

        assertTrue(res.getRejectionReasons().contains("EMI_EXCEEDS_50_PERCENT"));
    }

    // ✅ RISK BAND TEST
    @Test
    void shouldAssignCorrectRiskBand() {
        LoanApplicationRequest req = validRequest();
        req.getApplicant().setCreditScore(700);

        LoanApplicationResponse res = service.checkApplication(req);

        assertEquals(RiskBand.MEDIUM, res.getRiskBand());
    }
}