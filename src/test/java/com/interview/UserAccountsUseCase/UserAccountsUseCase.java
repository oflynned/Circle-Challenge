package com.interview.UserAccountsUseCase;


import com.github.arteam.jdit.DBIRunner;
import com.github.arteam.jdit.annotations.DataSet;
import com.interview.MoneyTransferUseCase.Exceptions.*;
import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDao;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDaoImpl;
import com.interview.UserAccountsUseCase.Model.Account;
import com.interview.UserAccountsUseCase.Presenter.AccountDao;
import com.interview.UserAccountsUseCase.Presenter.AccountDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.skife.jdbi.v2.DBI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@RunWith(DBIRunner.class)
@DataSet("schema.sql")
public class UserAccountsUseCase {
    private AccountDaoImpl accountDao;

    private void setup() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost:5432");
        dataSource.setDatabaseName("circle");
        dataSource.setUser("circle");
        dataSource.setPassword("circle");

        DBI dbi = new DBI(dataSource);
        AccountDao accountDao = dbi.onDemand(AccountDao.class);
        this.accountDao = new AccountDaoImpl(accountDao);
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_exception_on_fail_where_id_does_not_exist() {
        setup();

        try {
            accountDao.getAccount("-1");
            fail("Non-existing user ID exists");
        } catch (TransactionIdException e) {
            e.printStackTrace();
        }
    }

    @DataSet("accounts.sql")
    @Test
    public void assert_pass_where_id_exists() {
        setup();

        try {
            accountDao.getAccount("1");
        } catch (TransactionIdException e) {
            e.printStackTrace();
            fail("Existing user ID doesn't exist");
        }
    }
}