package com.revolut.rest.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccTransferPayload {

    @JsonProperty
    String toAccountNo;

    @JsonProperty
    BigDecimal amount;

    @JsonProperty
    String fromAccountNo;
}
