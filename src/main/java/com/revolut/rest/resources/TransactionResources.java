package com.revolut.rest.resources;

import com.codahale.metrics.annotation.Timed;
import com.revolut.library.TransactionRuntime;
import com.revolut.library.protocol.CustomerInformation;
import com.revolut.rest.protocol.AccTransferPayload;
import com.revolut.rest.protocol.UserDefinationPayload;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResources {
    public final TransactionRuntime transactionRuntime;

    public TransactionResources(TransactionRuntime transactionRuntime) {
        this.transactionRuntime = transactionRuntime;
    }

    @GET
    @Timed
    @Path("/customer/details")
    public String accountDetails(@QueryParam("accountNumber") String accountNumber) {

        return transactionRuntime.getAccountDetails(accountNumber);

    }

    @POST
    @Timed
    @Path("/customer/register")
    public CustomerInformation nextCount(UserDefinationPayload payload, @Context HttpHeaders httpHeaders) {

        return transactionRuntime.registerUser(payload);

    }

    @POST
    @Timed
    @Path("/customer/accountTransfer")
    public String accountTransfer(AccTransferPayload payload, @Context HttpHeaders httpHeaders) {

        return transactionRuntime.accountTransfer(payload);

    }

    @POST
    @Timed
    @Path("/customer/selfDeposit")
    public String selfDeposit(@QueryParam("accountNumber") String accountNumber, @QueryParam("amount") BigDecimal amount) {
        return transactionRuntime.selfDeposit(accountNumber, amount);
    }

    @POST
    @Timed
    @Path("/customer/selfWithdraw")
    public String selfWithdraw(@QueryParam("accountNumber") String accountNumber, @QueryParam("amount") BigDecimal amount) {
        return transactionRuntime.selfWidthdraw(accountNumber, amount);
    }

    @GET
    @Timed
    @Path("/customer/rollBack")
    public String rollBack() {
        return transactionRuntime.rollBack();
    }
}