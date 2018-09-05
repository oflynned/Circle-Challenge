package com.interview.MoneyTransferUseCase.Presenter;

import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;
import com.interview.UserAccountsUseCase.Presenter.AccountDao;
import com.interview.MoneyTransferUseCase.Exceptions.*;

import java.util.List;

public class TransactionDaoImpl implements TransactionDao {

    private TransactionDao transactionDao;
    private AccountDao accountDao;

    public TransactionDaoImpl(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    @Override
    public void makeTransaction(MoneyTransaction transaction)
            throws TransactionIdException, MinTransferExceededException, MaxTransferExceededException,
            MaxBalanceExceededException, InsufficientFundsException {
        if (transaction.getToId().equals(transaction.getFromId()))
            throw new TransactionIdException("Recipient id cannot be the same as the sender");
        if (transaction.getAmount() < 1)
            throw new MinTransferExceededException("Transfer amount below minimum limit");
        if (transaction.getAmount() > 999_999)
            throw new MaxTransferExceededException("Transfer amount above maximum limit");

        int senderBalance = accountDao.getAccount(transaction.getFromId()).getBalance();
        int recipientBalance = accountDao.getAccount(transaction.getToId()).getBalance();

        if (recipientBalance + transaction.getAmount() > 999_999)
            throw new MaxBalanceExceededException("Recipient's max account limit exceeded");

        if (senderBalance < transaction.getAmount())
            throw new InsufficientFundsException("Sender has insufficient funds");

        transactionDao.makeTransaction(transaction);
    }

    @Override
    public List<MoneyTransaction> getTransactionsForUserId(String id) {
        return transactionDao.getTransactionsForUserId(id);
    }

    @Override
    public int getAmountSent(String id) {
        return transactionDao.getAmountSent(id);
    }

    @Override
    public int getAmountReceived(String id) {
        return transactionDao.getAmountReceived(id);
    }

    @Override
    public List<MoneyTransaction> getTransactions() {
        return transactionDao.getTransactions();
    }
}
