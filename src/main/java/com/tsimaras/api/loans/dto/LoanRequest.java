package com.tsimaras.api.loans.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LoanRequest(
        @NotBlank
        @Size(min = 2, max = 100)
        String applicantName,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        @Min(6)
        @Max(360)
        Integer termMonths)
{}
