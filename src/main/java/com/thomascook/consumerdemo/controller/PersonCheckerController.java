package com.thomascook.consumerdemo.controller;

import com.thomascook.consumerdemo.PersonDTO;
import com.thomascook.consumerdemo.ResultResponse;
import com.thomascook.consumerdemo.service.SimpsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersonCheckerController {

    private final SimpsonService simpsonService;

    public static final String DNA_PATH = "/test";

    @PostMapping(DNA_PATH)
    public ResultResponse dnaTest(@RequestBody PersonDTO person) {
        return simpsonService.fetchInfo(person);
    }
}
