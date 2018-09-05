package com.interview.MoneyTransferUseCase.Presenter;

import com.interview.MoneyTransferUseCase.Exceptions.*;
import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(MoneyTransactionResultMapper.class)
public interface TransactionDao {
    @SqlUpdate("INSERT INTO transactions(from_id,to_id,amount,timestamp) VALUES(:t.fromId,:t.toId,:t.amount,:t.timestamp)")
    void makeTransaction(@BindBean("t") MoneyTransaction transaction)
            throws TransactionIdException, MinTransferExceededException, MaxTransferExceededException,
            MaxBalanceExceededException, InsufficientFundsException;

    @SqlQuery("SELECT * FROM transactions WHERE from_id=:id OR to_id=:id")
    List<MoneyTransaction> getTransactionsForUserId(@Bind("id") String id);

    @SqlQuery("SELECT SUM(amount) FROM transactions WHERE from_id=:id")
    int getAmountSent(@Bind("id") String id);

    @SqlQuery("SELECT SUM(amount) FROM transactions WHERE to_id=:id")
    int getAmountReceived(@Bind("id") String id);

    @SqlQuery("SELECT * FROM transactions")
    List<MoneyTransaction> getTransactions();
}
