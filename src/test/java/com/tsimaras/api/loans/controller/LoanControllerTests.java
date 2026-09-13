package com.tsimaras.api.loans.controller;

import com.tsimaras.api.loans.dto.LoanResponse;
import com.tsimaras.api.loans.dto.LoanStatus;
import com.tsimaras.api.loans.exception.LoanAmountExceededException;
import com.tsimaras.api.loans.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
public class LoanControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LoanService loanService;

    @Test
    public void createLoan_whenAmountIsOver100_000_returnsBadRequest() throws Exception {
        when(loanService.createLoan(any()))
                .thenThrow(new LoanAmountExceededException("Loan amount shouldn't exceed 100.000€"));

        mockMvc.perform(post("/api/loans")
                        .contentType("application/json")
                        .content("""
                {
                  "applicantName": "user1",
                  "amount": 150000,
                  "termMonths": 48
                }
                """))
                .andExpect(status().isBadRequest()).andExpect(content().string("Loan amount shouldn't exceed 100.000€"));
    }

    @Test
    public void createLoan_whenSuccess_ReturnsIsCreated() throws Exception {
        LoanResponse loanResponse = new LoanResponse(1L, "user1", new BigDecimal(10_000), 48,
                LoanStatus.APPROVED);
        when(loanService.createLoan(any())).thenReturn(loanResponse);

        mockMvc.perform(post("/api/loans")
                        .contentType("application/json")
                        .content("""
                {
                  "applicantName": "user1",
                  "amount": 10000,
                  "termMonths": 48
                }
                """))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.applicantName").value("user1"))
                .andExpect(jsonPath("$.amount").value(10000))
                .andExpect(jsonPath("$.termMonths").value(48))
                .andExpect(jsonPath("$.status").value(LoanStatus.APPROVED.toString()));
    }

    @Test
    public void createLoan_whenRequestIsInvalid_returnsBadRequestAndDoesNotCallService() throws Exception {
        // TODO: implement
    }

    // TODO: implement tests for whole CRUD.

}
