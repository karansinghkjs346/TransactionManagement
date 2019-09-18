package com.revolut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.revolut.config.TransactionManagementConfig;
import com.revolut.library.TransactionRuntime;
import com.revolut.rest.protocol.AccTransferPayload;
import com.revolut.rest.protocol.UserDefinationPayload;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class UnitTestBase {
    TransactionRuntime transactionRuntime;
    static ObjectMapper MAPPER=new ObjectMapper(new YAMLFactory());
    static TransactionManagementConfig transactionManagementConfig=null;

     @BeforeClass
    public static void setUpClass() {
         try {
             transactionManagementConfig = MAPPER.readValue(UnitTestBase.class.getClassLoader().getResource("transactionManagement.yml"), TransactionManagementConfig.class);

         }
         catch (IOException e)
         {
             e.printStackTrace();
         }
     }

     public void setupTest()
     {
         transactionRuntime=new TransactionRuntime(transactionManagementConfig);
     }

     @Before
    public void setUpTest(){ this.setupTest();}

    protected UserDefinationPayload getSampleUserDefinationPayload()
    {
        UserDefinationPayload userDefinationPayload=new UserDefinationPayload();
        userDefinationPayload.setFirstName("Test");
        userDefinationPayload.setLastName("Testing");
        userDefinationPayload.setAmount(BigDecimal.valueOf(100));
        userDefinationPayload.setPhoneNumber(123456789);
        return userDefinationPayload;
    }

    protected UserDefinationPayload getSampleUserDefinationPayloadSecond()
    {
        UserDefinationPayload userDefinationPayload=new UserDefinationPayload();
        userDefinationPayload.setFirstName("TestFirst");
        userDefinationPayload.setLastName("TestingFirst");
        userDefinationPayload.setAmount(BigDecimal.valueOf(100));
        userDefinationPayload.setPhoneNumber(123456781);
        return userDefinationPayload;
    }


}
