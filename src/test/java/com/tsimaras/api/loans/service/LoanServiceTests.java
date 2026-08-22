package com.tsimaras.api.loans.service;

import com.tsimaras.api.loans.dto.LoanRequest;
import com.tsimaras.api.loans.dto.LoanResponse;
import com.tsimaras.api.loans.dto.LoanStatus;
import com.tsimaras.api.loans.entity.Loan;
import com.tsimaras.api.loans.exception.LoanAmountExceededException;
import com.tsimaras.api.loans.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTests {

    @Mock
    LoanRepository repository;

    @InjectMocks
    LoanService loanService;

    @Test
    public void createLoan_whenAmountIsEqualsOrUnder25_000_returnsApprovedLoan() {
        when(repository.save(any())).thenAnswer(invocation -> {
            Loan loan = invocation.getArgument(0);
            loan.setId(1L);
            return loan;
        });

        LoanRequest lr = new LoanRequest("user1", new BigDecimal(10_000), 48);
        LoanResponse loanResult = loanService.createLoan(lr);

        assertThat(loanResult.id()).isEqualTo(1L);
        assertThat(loanResult.applicantName()).isEqualTo(lr.applicantName());
        assertThat(loanResult.amount()).isEqualTo(lr.amount());
        assertThat(loanResult.termMonths()).isEqualTo(lr.termMonths());
        assertThat(loanResult.status()).isEqualTo(LoanStatus.APPROVED);
    }

    @Test
    public void createLoan_whenAmountIsOver25_000_returnsManualReviewLoan() {
        when(repository.save(any())).thenAnswer(invocation -> {
            Loan loan = invocation.getArgument(0);
            loan.setId(1L);
            return loan;
        });
        LoanRequest lr = new LoanRequest("user1", new BigDecimal(55_000), 48);
        LoanResponse loanResult = loanService.createLoan(lr);

        assertThat(loanResult.id()).isEqualTo(1L);
        assertThat(loanResult.applicantName()).isEqualTo(lr.applicantName());
        assertThat(loanResult.amount()).isEqualTo(lr.amount());
        assertThat(loanResult.termMonths()).isEqualTo(lr.termMonths());
        assertThat(loanResult.status()).isEqualTo(LoanStatus.MANUAL_REVIEW);
    }

    @Test
    public void createLoan_whenAmountIsOver100_000_throws() {
        LoanRequest lr = new LoanRequest("user1", new BigDecimal(150_000), 48);
        assertThrows(LoanAmountExceededException.class,() -> loanService.createLoan(lr));
        verify(repository, never()).save(any());
    }

    @Test
    public void toLoan_whenAmountIsUnderOrEqualTo25_000_returnsStatusApproved() {
        LoanRequest lrUnder = new LoanRequest("user1", new BigDecimal(10_000), 48);
        LoanRequest lrEquals = new LoanRequest("user1", new BigDecimal(25_000), 48);
        LoanRequest lrOver = new LoanRequest("user1", new BigDecimal(26_000), 48);

        Loan loanUnder = loanService.toLoan(lrUnder);
        Loan loanEquals = loanService.toLoan(lrEquals);
        Loan loanOver = loanService.toLoan(lrOver);

        assertThat(loanUnder.getStatus()).isEqualTo((LoanStatus.APPROVED));
        assertThat(loanEquals.getStatus()).isEqualTo((LoanStatus.APPROVED));
        assertThat(loanOver.getStatus()).isNotEqualTo((LoanStatus.APPROVED));
    }

    @Test
    public void toLoan_whenAmountIsOver25_000_returnsStatusManualReview() {
        LoanRequest lrOver = new LoanRequest("user1", new BigDecimal(25_001), 48);

        Loan loanOver = loanService.toLoan(lrOver);
        assertThat(loanOver.getStatus()).isEqualTo((LoanStatus.MANUAL_REVIEW));
    }
}
