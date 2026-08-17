package com.tsimaras.api.loans.service;

import com.tsimaras.api.loans.dto.LoanRequest;
import com.tsimaras.api.loans.dto.LoanResponse;
import com.tsimaras.api.loans.dto.LoanStatus;
import com.tsimaras.api.loans.entity.Loan;
import com.tsimaras.api.loans.exception.LoanAmountExceededException;
import com.tsimaras.api.loans.repository.LoanRepository;
import org.springframework.stereotype.Service;

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
}
