package com.tsimaras.api.loans.exception;

public class LoanAmountExceededException extends RuntimeException {

    public LoanAmountExceededException(String msg) {
        super(msg);
    }
}
