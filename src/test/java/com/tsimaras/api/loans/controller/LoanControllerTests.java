package com.tsimaras.api.loans.controller;

import com.tsimaras.api.loans.exception.LoanAmountExceededException;
import com.tsimaras.api.loans.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().isBadRequest());
    }

}
