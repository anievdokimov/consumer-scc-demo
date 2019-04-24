package com.thomascook.consumerdemo.service;

import com.thomascook.consumerdemo.PersonDTO;
import com.thomascook.consumerdemo.ResultResponse;
import com.thomascook.consumerdemo.adapter.DnaRequestDTO;
import com.thomascook.consumerdemo.adapter.DnaResponseDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Setter
public class SimpsonService {

    @Value("${dna.api.url}")
    public String dnaServiceHost;

    public final static String dnaPath = "/dna-test";

    public ResultResponse fetchInfo(PersonDTO person) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<DnaResponseDTO> response = restTemplate.exchange(
                RequestEntity
                        .post(URI.create(dnaServiceHost + dnaPath))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(new DnaRequestDTO(person.getName()))
                , DnaResponseDTO.class);

        return new ResultResponse(
                response.getBody().getName(),
                response.getBody().getAge(),
                response.getBody().isSimpson());
    }
}
