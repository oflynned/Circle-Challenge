package com.interview.MoneyTransferUseCase.Exceptions;

public class MaxBalanceExceededException extends Exception {
    public MaxBalanceExceededException(String message) {
        super(message);
    }
}
