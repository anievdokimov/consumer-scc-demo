package com.thomascook.consumerdemo;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.DefaultResponseValues;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslResponse;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.PactSpecVersion;
import au.com.dius.pact.model.RequestResponsePact;
import com.google.common.collect.ImmutableMap;
import com.thomascook.consumerdemo.service.SimpsonService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpsonInfoConsumerTest extends AbstractConsumerTest {
    @Autowired
    private SimpsonService simpsonService;

    @DefaultResponseValues
    public void defaultResponseValues(PactDslResponse response) {
        Map<String, String> headers = ImmutableMap.of(
                "Content-Type", "application/json"
        );
        response.headers(headers);
    }

    public static PactDslJsonBody prepareRequestBody() {
        return new PactDslJsonBody()
                .stringMatcher("name", "(homer|bart)", "homer")
                .close()
                .asBody();
    }

    public static PactDslJsonBody prepareResponseBody() {
        return new PactDslJsonBody()
                .stringType("name", "homer")
                .integerType("age", 12)
                .booleanType("isSimpson", false)
                .close()
                .asBody();
    }

    @Test
    public void shouldBeAValidContractForSimpsonFamily() {
        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("demo-consumer")
                .hasPactWith("demo-provider")
                .given("A Star Wars character named Luke Skywalker and ID luke", ImmutableMap.of("id", "luke"))
                .uponReceiving("Fetch character personal information")
                .path("/dna-test")
                .method("POST")
                .body(prepareRequestBody())
                .willRespondWith()
                .status(200)
                .body(prepareResponseBody())
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault(PactSpecVersion.V3);
        PactVerificationResult result = runConsumerTest(pact, config, mockServer -> {
            simpsonService.setDnaServiceHost(mockServer.getUrl());
            ResultResponse personalInfo = simpsonService.fetchInfo(new PersonDTO("homer"));
            assertThat(personalInfo.getName()).isEqualTo("homer");
            assertThat(personalInfo.getAge()).isEqualTo(12);
            Assert.assertNotNull(personalInfo.isSimpson());
        });

        checkResult(result);
    }
}
