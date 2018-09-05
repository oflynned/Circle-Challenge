package com.interview.MoneyTransferUseCase.Exceptions;

public class MaxTransferExceededException extends Exception {
    public MaxTransferExceededException(String message) {
        super(message);
    }
}
