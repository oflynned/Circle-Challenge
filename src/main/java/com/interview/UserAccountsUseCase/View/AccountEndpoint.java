package com.interview.UserAccountsUseCase.View;


import javax.ws.rs.core.Response;

public interface AccountEndpoint {
    Response getAccountById(String id);

    Response getAccounts();
}
