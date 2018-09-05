package com.interview.MoneyTransferUseCase;


import com.github.arteam.jdit.DBIRunner;
import com.github.arteam.jdit.annotations.DataSet;
import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDao;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDaoImpl;
import com.interview.UserAccountsUseCase.Presenter.AccountDao;
import com.interview.UserAccountsUseCase.Presenter.AccountDaoImpl;
import com.interview.MoneyTransferUseCase.Exceptions.*;
import com.interview.UserAccountsUseCase.Model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.skife.jdbi.v2.DBI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@RunWith(DBIRunner.class)
@DataSet("schema.sql")
public class MoneyTransactionResourceTest {
    private AccountDaoImpl accountDao;
    private TransactionDaoImpl transactionDao;

    private void setup() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost:5432");
        dataSource.setDatabaseName("circle");
        dataSource.setUser("circle");
        dataSource.setPassword("circle");

        DBI dbi = new DBI(dataSource);
        AccountDao accountDao = dbi.onDemand(AccountDao.class);
        TransactionDao transactionDao = dbi.onDemand(TransactionDao.class);

        this.accountDao = new AccountDaoImpl(accountDao);
        this.transactionDao = new TransactionDaoImpl(transactionDao, accountDao);
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_with_sender_and_receiver_having_enough_money_and_avoiding_limits() throws TransactionIdException {
        setup();

        Account senderBeforeTransaction = accountDao.getAccount("1");
        Account recipientBeforeTransaction = accountDao.getAccount("2");

        assertEquals(senderBeforeTransaction.getBalance(), 100);
        assertEquals(recipientBeforeTransaction.getBalance(), 120);

        MoneyTransaction transaction = new MoneyTransaction("1", "2", 100);
        try {
            transactionDao.makeTransaction(transaction);
        } catch (TransactionIdException | MinTransferExceededException | MaxBalanceExceededException |
                MaxTransferExceededException | InsufficientFundsException e) {
            e.printStackTrace();
        }

        Account senderAfterTransaction = accountDao.getAccount("1");
        Account recipientAfterTransaction = accountDao.getAccount("2");

        assertEquals(senderAfterTransaction.getBalance(), 0);
        assertEquals(recipientAfterTransaction.getBalance(), 220);
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_fails_where_sender_and_recipient_have_the_same_id() throws TransactionIdException {
        setup();

        Account sender = accountDao.getAccount("1");
        Account recipient = accountDao.getAccount("1");
        MoneyTransaction transaction = new MoneyTransaction(sender.getId(), recipient.getId(), 1);

        assertEquals(sender.getId(), recipient.getId());

        try {
            transactionDao.makeTransaction(transaction);
            fail("Same IDs reported as being different");
        } catch (TransactionIdException | MaxBalanceExceededException | MaxTransferExceededException |
                InsufficientFundsException | MinTransferExceededException e) {
            e.printStackTrace();
        }
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_failed_with_negative_transaction_amounts() throws TransactionIdException {
        setup();

        Account sender = accountDao.getAccount("1");
        Account recipient = accountDao.getAccount("2");
        MoneyTransaction transaction = new MoneyTransaction(sender.getId(), recipient.getId(), -1);

        try {
            transactionDao.makeTransaction(transaction);
            fail("Negative transaction amount allowed");
        } catch (TransactionIdException | MinTransferExceededException | MaxBalanceExceededException |
                MaxTransferExceededException | InsufficientFundsException e) {
            e.printStackTrace();
        }
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_fails_with_recipient_funds_exceeding_minimum_limit() throws TransactionIdException {
        setup();

        Account sender = accountDao.getAccount("1");
        Account recipient = accountDao.getAccount("2");
        MoneyTransaction transaction = new MoneyTransaction(sender.getId(), recipient.getId(), 0);

        try {
            transactionDao.makeTransaction(transaction);
            fail("Transaction below minimum limit allowed");
        } catch (TransactionIdException | MinTransferExceededException | MaxBalanceExceededException |
                MaxTransferExceededException | InsufficientFundsException e) {
            e.printStackTrace();
        }
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_fails_with_recipient_funds_exceeding_maximum_limit() throws TransactionIdException {
        setup();

        Account sender = accountDao.getAccount("1");
        Account recipient = accountDao.getAccount("2");
        MoneyTransaction transaction = new MoneyTransaction(sender.getId(), recipient.getId(), 1_000_000);

        try {
            transactionDao.makeTransaction(transaction);
            fail("Transaction above maximum limit allowed");
        } catch (TransactionIdException | MinTransferExceededException | MaxBalanceExceededException |
                MaxTransferExceededException | InsufficientFundsException e) {
            e.printStackTrace();
        }
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_fails_with_recipient_sending_funds_over_max_limit() throws TransactionIdException {
        setup();

        Account sender = accountDao.getAccount("4");
        Account recipient = accountDao.getAccount("1");
        MoneyTransaction transaction = new MoneyTransaction(sender.getId(), recipient.getId(), 1_000_000);

        try {
            transactionDao.makeTransaction(transaction);
            fail("Balance above maximum limit allowed");
        } catch (TransactionIdException | MinTransferExceededException | MaxBalanceExceededException |
                MaxTransferExceededException | InsufficientFundsException e) {
            e.printStackTrace();
        }
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_transaction_fails_with_sender_having_insufficient_funds() throws TransactionIdException {
        setup();

        Account sender = accountDao.getAccount("1");
        Account recipient = accountDao.getAccount("2");
        MoneyTransaction transaction = new MoneyTransaction(sender.getId(), recipient.getId(), 101);

        try {
            transactionDao.makeTransaction(transaction);
            fail("Insufficient funds allowed to go through");
        } catch (TransactionIdException | MinTransferExceededException | MaxBalanceExceededException |
                MaxTransferExceededException | InsufficientFundsException e) {
            e.printStackTrace();
        }
    }
}