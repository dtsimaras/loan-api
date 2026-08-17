package com.tsimaras.api.loans.controller;

import com.tsimaras.api.loans.dto.LoanRequest;
import com.tsimaras.api.loans.dto.LoanResponse;
import com.tsimaras.api.loans.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/health")
    public String getHealth() {
        return "Loan API is running";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable String id) {
        return "Loan requested: " + id;
    }

    @PostMapping
    public LoanResponse createLoan(@Valid @RequestBody LoanRequest loanRequest) {

        return loanService.createLoan(loanRequest);
    }
}
