package com.thomascook.consumerdemo;

import au.com.dius.pact.consumer.PactVerificationResult;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractConsumerTest {

    void checkResult(PactVerificationResult result) {
        if (result instanceof PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error) result).getError());
        }
        assertThat(PactVerificationResult.Ok.INSTANCE).isEqualTo(result);
    }

}