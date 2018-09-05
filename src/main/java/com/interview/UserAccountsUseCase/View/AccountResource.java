package com.interview.UserAccountsUseCase.View;

import com.interview.MoneyTransferUseCase.Presenter.TransactionDao;
import com.interview.MoneyTransferUseCase.Exceptions.TransactionIdException;
import com.interview.UserAccountsUseCase.Presenter.AccountDao;
import com.interview.UserAccountsUseCase.Model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * http://www.dropwizard.io/1.0.6/docs/manual/core.html#resources
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource implements AccountEndpoint {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    public AccountResource(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    @GET
    @Path("/ping")
    @Produces("text/plain")
    public String ping() {
        return "pong";
    }

    @GET
    @Path("/accounts/{id}")
    @Override
    public Response getAccountById(@PathParam("id") String id) {
        try {
            return Response.ok().entity(accountDao.getAccount(id)).build();
        } catch (TransactionIdException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/accounts")
    @Override
    public Response getAccounts() {
        List<Account> accounts = accountDao.getAccounts();

        for (Account account : accounts) {
            int moneySent = transactionDao.getAmountSent(account.getId());
            int moneyReceived = transactionDao.getAmountReceived(account.getId());
            int balance = account.getBalance();

            account.setBalance(balance + moneyReceived - moneySent);
        }

        return Response.ok().entity(accounts).build();
    }
}
