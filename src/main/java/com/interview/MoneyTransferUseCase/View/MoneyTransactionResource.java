package com.interview.MoneyTransferUseCase.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.MoneyTransferUseCase.Exceptions.*;
import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MoneyTransactionResource implements MoneyTransactionEndpoint {
    private final TransactionDao transactionDao;

    public MoneyTransactionResource(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @GET
    @Path("/transactions")
    @Override
    public List<MoneyTransaction> getAllTransactions() {
        return transactionDao.getTransactions();
    }

    @GET
    @Path("/transactions/{id}")
    @Override
    public List<MoneyTransaction> getTransactions(@PathParam("id") String id) {
        return transactionDao.getTransactionsForUserId(id);
    }

    @POST
    @Path("/transactions")
    @Override
    public Response makeTransaction(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MoneyTransaction transaction = mapper.readValue(body, MoneyTransaction.class);

        try {
            transactionDao.makeTransaction(transaction);
        } catch (TransactionIdException | MaxTransferExceededException | MinTransferExceededException e) {
            e.printStackTrace();
            return Response.status(400).entity("{\"reason\":\"" + e.getMessage() + "\"}").build();
        } catch (InsufficientFundsException | MaxBalanceExceededException e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"reason\":\"" + e.getMessage() + "\"}").build();
        }

        return Response.ok().build();
    }
}
