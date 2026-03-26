package com.interview.loanApplication.controller;

import com.interview.loanApplication.dto.request.*;
import com.interview.loanApplication.enums.*;
import com.interview.loanApplication.service.LoanApplicationService;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    
    @MockitoBean // 🔥 VERY IMPORTANT
    private LoanApplicationService service;

    private LoanApplicationRequest request() {
        ApplicantDTO a = new ApplicantDTO();
        a.setName("Test");
        a.setAge(30);
        a.setMonthlyIncome(BigDecimal.valueOf(50000));
        a.setEmploymentType(EmploymentType.SALARIED);
        a.setCreditScore(750);

        LoanDTO l = new LoanDTO();
        l.setAmount(BigDecimal.valueOf(200000));
        l.setTenureMonths(24);
        l.setPurpose(LoanPurpose.PERSONAL);

        LoanApplicationRequest req = new LoanApplicationRequest();
        req.setApplicant(a);
        req.setLoan(l);

        return req;
    }

    @Test
    void shouldReturn200() throws Exception {
        mockMvc.perform(post("/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request())))
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldReturn400ForInvalidInput() throws Exception {

        LoanApplicationRequest req = new LoanApplicationRequest(); // empty

        mockMvc.perform(post("/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }
    
    @Test
    void shouldHandleInvalidEnum() throws Exception {

        String invalidJson = """
        {
          "applicant": {
            "name": "Test",
            "age": 30,
            "monthlyIncome": 50000,
            "employmentType": "INVALID",
            "creditScore": 700
          },
          "loan": {
            "amount": 200000,
            "tenureMonths": 24,
            "purpose": "PERSONAL"
          }
        }
        """;

        mockMvc.perform(post("/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
