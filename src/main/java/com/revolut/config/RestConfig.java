package com.revolut.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestConfig extends Configuration
{
    @JsonProperty
    TransactionManagementConfig transactionManagementConfig;

    @JsonProperty
    public int hearbeat=100;

}

