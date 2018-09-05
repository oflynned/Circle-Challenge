package com.interview.MoneyTransferUseCase.Exceptions;

public class MinTransferExceededException extends Exception {
    public MinTransferExceededException(String message) {
        super(message);
    }
}
