package com.interview.UserAccountsUseCase.Presenter;

import com.interview.MoneyTransferUseCase.Exceptions.TransactionIdException;
import com.interview.UserAccountsUseCase.Model.Account;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(AccountResultMapper.class)
public interface AccountDao {
    @SqlQuery("SELECT accounts.id, " +
            "(SELECT balance FROM accounts WHERE id=:id) + " +
            "(SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE to_id=:id) - " +
            "(SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE from_id=:id) AS balance " +
            "FROM accounts WHERE id=:id " +
            "GROUP BY accounts.id")
    Account getAccount(@Bind("id") String id) throws TransactionIdException;

    @SqlQuery("SELECT * FROM accounts")
    List<Account> getAccounts();
}
