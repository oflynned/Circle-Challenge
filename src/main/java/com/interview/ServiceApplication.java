package com.interview;

import com.interview.Common.*;
import com.interview.MoneyTransferUseCase.View.MoneyTransactionResource;
import com.interview.MoneyTransferUseCase.Presenter.MoneyTransactionResultMapper;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDao;
import com.interview.MoneyTransferUseCase.Presenter.TransactionDaoImpl;
import com.interview.UserAccountsUseCase.Presenter.AccountDao;
import com.interview.UserAccountsUseCase.Presenter.AccountDaoImpl;
import com.interview.UserAccountsUseCase.View.AccountResource;
import com.interview.UserAccountsUseCase.Presenter.AccountResultMapper;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * http://www.dropwizard.io/1.0.6/docs/manual/core.html#application
 */
public class ServiceApplication extends Application<ServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new ServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new FlywayBundle<ServiceConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(ServiceConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        });
    }

    @Override
    public void run(ServiceConfiguration config, Environment env) throws Exception {
        // cleans database
        config.getFlywayFactory().build(config.getDataSourceFactory().build(env.metrics(), "db")).clean();

        // re-run flyway migrations
        config.getFlywayFactory().build(config.getDataSourceFactory().build(env.metrics(), "db")).migrate();

        // Datasource factory, jdbi connections
        final DBIFactory factory = new DBIFactory();
        final DBI dbi = factory.build(env, config.getDataSourceFactory(), "postgresql");

        // SQL callbacks
        AccountDao accountDao = dbi.onDemand(AccountDao.class);
        TransactionDao transactionDao = dbi.onDemand(TransactionDao.class);

        // Register Result Set Mapper for POJO Account with UUID id.
        dbi.registerMapper(new AccountResultMapper());
        dbi.registerMapper(new MoneyTransactionResultMapper());

        AccountDaoImpl accountDaoImpl = new AccountDaoImpl(accountDao);
        TransactionDaoImpl transactionDaoImpl = new TransactionDaoImpl(transactionDao, accountDao);

        AccountResource accountResource = new AccountResource(accountDaoImpl, transactionDao);
        MoneyTransactionResource transactionResource = new MoneyTransactionResource(transactionDaoImpl);

        //register your API resource here
        env.jersey().register(accountResource);
        env.jersey().register(transactionResource);
    }
}
