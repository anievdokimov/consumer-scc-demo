package com.thomascook.consumerdemo

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.REMOTE,
        repositoryRoot = "http://127.0.0.1:8081/nexus/content/repositories/snapshots",
        ids = "com.thomascook:producer-scc-demo:+:8090"
)
@AutoConfigureMockMvc
class ConsumerDemoTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Unroll
    def "check that #name is part of simpson family"() {

        given:
        String homerRequest = asJsonString(new PersonDTO(name))

        when:
        ResultActions resultActions = mockMvc.perform(post(PersonCheckerController.DNA_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(homerRequest))
        then:
        resultActions
                .andExpect(jsonPath('$.name').exists())
                .andExpect(jsonPath('$.name').value(name))
                .andExpect(jsonPath('$.simpson').value(true))
                .andExpect(jsonPath('$.age').exists())
                .andExpect(jsonPath('$.age').isNumber())

        where:
        name << ["homer", "lisa", "bart"]
    }

    def "check that Nelson is not a simpson"() {
        given:
        String name = "Nelson"
        String homerRequest = asJsonString(new PersonDTO(name))

        when:
        ResultActions resultActions = mockMvc.perform(post("/test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(homerRequest))
        then:
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').exists())
                .andExpect(jsonPath('$.name').value(name))
                .andExpect(jsonPath('$.simpson').value(false))
                .andExpect(jsonPath('$.age').doesNotExist())

    }

    private String asJsonString(Object o) throws Exception {
        JsonNode jsonNode = objectMapper.valueToTree(o)
        return objectMapper.writeValueAsString(jsonNode)
    }

    private static ObjectMapper objectMapper = new ObjectMapper()
}
