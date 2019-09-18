package com.revolut.health;

import com.codahale.metrics.health.HealthCheck;

public class TransactionManagementHealthCheck extends HealthCheck {
    private final String template;

    public TransactionManagementHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TransactionManagement");
        if (!saying.contains("TransactionManagement")) {
            return Result.unhealthy("TransactionManagement Unhealthy");
        }
        return Result.healthy();
    }
}