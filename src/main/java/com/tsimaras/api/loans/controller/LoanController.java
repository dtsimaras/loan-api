package com.tsimaras.api.loans.controller;

import com.tsimaras.api.loans.dto.LoanRequest;
import com.tsimaras.api.loans.dto.LoanResponse;
import com.tsimaras.api.loans.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse createLoan(@Valid @RequestBody LoanRequest loanRequest) {
        return loanService.createLoan(loanRequest);
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> getLoans() {
        return ResponseEntity.ok(loanService.getLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
        LoanResponse loanR = loanService.getLoanById(id);
        return ResponseEntity.ok(loanR);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LoanResponse updateLoan(@PathVariable Long id, @RequestBody LoanRequest loanRequest) {
        return loanService.updateLoan(loanRequest, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
    }
}
