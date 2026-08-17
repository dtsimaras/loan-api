package com.tsimaras.api.loans.entity;

import com.tsimaras.api.loans.dto.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Loan {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String applicantName;

    private BigDecimal amount;

    private Integer termMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    LocalDateTime createdAt;

    public Loan() {
        this.createdAt = LocalDateTime.now();
    }
}
