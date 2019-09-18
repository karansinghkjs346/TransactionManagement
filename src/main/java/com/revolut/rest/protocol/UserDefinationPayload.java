package com.revolut.rest.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDefinationPayload {

    @JsonProperty
    String firstName;

    @JsonProperty
    String lastName;

    @JsonProperty
    int phoneNumber;

    @JsonProperty
    BigDecimal amount;

}
