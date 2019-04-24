package com.thomascook.consumerdemo;

import com.thomascook.consumerdemo.adapter.DnaRequestDTO;
import com.thomascook.consumerdemo.adapter.DnaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class PersonCheckerController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dna.api.url}")
    private String dnaApiPath;

    public static final String DNA_PATH = "/test";

    @PostMapping(DNA_PATH)
    public ResultResponse dnaTest(@RequestBody PersonDTO person) {
        ResponseEntity<DnaResponseDTO> response = this.restTemplate.exchange(
                RequestEntity
                        .post(URI.create(dnaApiPath))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DnaRequestDTO(person.getName()))
                , DnaResponseDTO.class);

        return new ResultResponse(
                response.getBody().getName(),
                response.getBody().getAge(),
                response.getBody().isSimpson());
    }
}
