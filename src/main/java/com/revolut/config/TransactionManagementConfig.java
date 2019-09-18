package com.revolut.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class TransactionManagementConfig {

    @JsonProperty
    private String mode;



    public static class Provider {
        @Getter(AccessLevel.PRIVATE)
        @Setter(AccessLevel.PRIVATE)
        public static TransactionManagementConfig INSTANCE;

        /**
         * @return
         */
        @JsonIgnore
        public static TransactionManagementConfig get() {
            if (INSTANCE != null)
                return INSTANCE;
            else
                throw new RuntimeException("Config was used before it was intialized");

        }

        /**
         * @param config
         */
        @JsonIgnore
        public static void initialize(TransactionManagementConfig config) {
            INSTANCE = config;
        }
    }
}