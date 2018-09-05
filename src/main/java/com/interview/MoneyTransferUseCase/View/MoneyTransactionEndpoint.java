package com.interview.MoneyTransferUseCase.View;

import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public interface MoneyTransactionEndpoint {
    List<MoneyTransaction> getAllTransactions();

    List<MoneyTransaction> getTransactions(String id);

    Response makeTransaction(String body) throws IOException;
}
