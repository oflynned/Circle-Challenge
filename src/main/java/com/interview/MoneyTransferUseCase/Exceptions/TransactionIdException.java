package com.interview.MoneyTransferUseCase.Exceptions;

public class TransactionIdException extends Exception {
    public TransactionIdException(String message) {
        super(message);
    }
}
