package com.interview.UserAccountsUseCase.Presenter;

import com.interview.MoneyTransferUseCase.Exceptions.TransactionIdException;
import com.interview.UserAccountsUseCase.Model.Account;

import java.util.List;

public class AccountDaoImpl implements AccountDao {
    private AccountDao accountDao;

    public AccountDaoImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account getAccount(String id) throws TransactionIdException {
        Account account = accountDao.getAccount(id);

        if (account == null)
            throw new TransactionIdException("User doesn't exist");

        return account;
    }

    @Override
    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }
}
