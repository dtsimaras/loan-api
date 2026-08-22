package com.tsimaras.api.loans.dto;

import java.math.BigDecimal;

public record LoanResponse(
        Long id,
        String applicantName,
        BigDecimal amount,
        Integer termMonths,
        LoanStatus status
) {
}
