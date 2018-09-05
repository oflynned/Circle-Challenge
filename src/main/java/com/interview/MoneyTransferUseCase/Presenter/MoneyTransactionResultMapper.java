package com.interview.MoneyTransferUseCase.Presenter;

import com.interview.MoneyTransferUseCase.Model.MoneyTransaction;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyTransactionResultMapper implements ResultSetMapper<MoneyTransaction> {
    @Override
    public MoneyTransaction map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new MoneyTransaction(
                r.getString("id"),
                r.getString("from_id"),
                r.getString("to_id"),
                r.getLong("timestamp"),
                r.getInt("amount"));
    }
}
