package com.revolut;

import com.revolut.config.RestConfig;
import com.revolut.health.TransactionManagementHealthCheck;
import com.revolut.library.TransactionRuntime;
import com.revolut.rest.resources.TransactionResources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TransactionManagement extends Application<RestConfig> {

    public static void main(String[] args) throws Exception {
        new TransactionManagement().run(args);
    }

    @Override
    public String getName() {
        return "Transaction Management";
    }

    @Override
    public void initialize(Bootstrap<RestConfig> bootstrap) {
        // nothing to do yet
    }

    /**
     * @param restConfig
     * @param environment
     */
    @Override
    public void run(RestConfig restConfig,
                    Environment environment) {
        // nothing to do yet
        TransactionRuntime transactionRuntime = new TransactionRuntime(restConfig.getTransactionManagementConfig());

        final TransactionResources resources = new TransactionResources(transactionRuntime);
        final TransactionManagementHealthCheck healthCheck =
                new TransactionManagementHealthCheck("Working Transaction");
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resources);
    }


}
