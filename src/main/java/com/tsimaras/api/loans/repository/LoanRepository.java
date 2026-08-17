package com.tsimaras.api.loans.repository;

import com.tsimaras.api.loans.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
