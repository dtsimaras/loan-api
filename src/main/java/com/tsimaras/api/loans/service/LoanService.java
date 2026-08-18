package com.tsimaras.api.loans.service;

import com.tsimaras.api.loans.dto.LoanRequest;
import com.tsimaras.api.loans.dto.LoanResponse;
import com.tsimaras.api.loans.dto.LoanStatus;
import com.tsimaras.api.loans.entity.Loan;
import com.tsimaras.api.loans.exception.LoanAmountExceededException;
import com.tsimaras.api.loans.exception.LoanNotFoundException;
import com.tsimaras.api.loans.repository.LoanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public LoanResponse createLoan(LoanRequest loanRequest) {

        if (loanRequest.amount().doubleValue() > 100_000) {
            throw new LoanAmountExceededException("Loan amount shouldn't exceed 100.000€");
        }

        Loan savedLoan = loanRepository.save(toLoan(loanRequest));

        return toLoanResponse(savedLoan);
    }

    public Loan toLoan(LoanRequest lr) {
        Loan loan = new Loan();
        loan.setApplicantName(lr.applicantName());
        loan.setAmount(lr.amount());
        loan.setTermMonths(lr.termMonths());
        // TODO: this business logic is messy here
        loan.setStatus(lr.amount().doubleValue() <= 25_000 ? LoanStatus.APPROVED : LoanStatus.MANUAL_REVIEW);

        return loan;
    }

    public LoanResponse toLoanResponse(Loan loan) {
        return new LoanResponse(
                loan.getApplicantName(),
                loan.getAmount(),
                loan.getTermMonths(),
                loan.getStatus()
        );
    }

    public List<LoanResponse> getLoans() {
        return loanRepository.findAll().stream().map(this::toLoanResponse).toList();
    }

    public LoanResponse getLoanById(Long id) {
        Optional<Loan> loan = loanRepository.findById(id);

        if (loan.isPresent()) {
            return toLoanResponse(loan.get());
        } else {
            throw new LoanNotFoundException("Loan with id " + id + " does not exist");
        }
    }

    public LoanResponse updateLoan(LoanRequest loanRequest, Long id) {
        Loan loan = toLoan(loanRequest);
        // TODO: status is set by luck when logic will be removed from toLoan take note
        Optional<Loan> loanOptional = loanRepository.findById(id);

        if (loanOptional.isPresent()) {
            loan.setId(id);
            return toLoanResponse(loanRepository.save(loan));
        } else {
            throw new LoanNotFoundException("Loan with id " + id + " does not exist");
        }
    }
}
