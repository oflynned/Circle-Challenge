package com.interview.UserAccountsUseCase.Presenter;

import com.interview.UserAccountsUseCase.Model.Account;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountResultMapper implements ResultSetMapper<Account> {

    @Override
    public Account map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Account(r.getString("id"), r.getInt("balance"));
    }
}
