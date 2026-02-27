package com.piggymetrics.account.client;

import com.piggymetrics.account.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author cdov
 */
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties = {
        "feign.hystrix.enabled=true"
})
public class StatisticsServiceClientFallbackTest {
    @Autowired
    private StatisticsServiceClient statisticsServiceClient;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testUpdateStatisticsWithFailFallback(CapturedOutput output){
        statisticsServiceClient.updateStatistics("test", new Account());
        assertTrue(output.getOut().contains("Error during update statistics for account: test"));
    }

}

