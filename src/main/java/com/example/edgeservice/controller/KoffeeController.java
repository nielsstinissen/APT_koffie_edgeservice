package com.example.edgeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class KoffeeController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${koffiedrankenservice.baseurl}")
    private String koffieDrankenServiceBaseUrl;

    @Value(("${koffiebonenservice.baseurl}"))
    private String koffieBonenServiceBaseUrl;

    @Value(("${koffiegerechtenservice.baseurl}"))
    private String koffieGerechtenServiceBaseUrl;

    @GetMapping("/AlleKoffie/naam={naam}")

    @GetMapping("/AlleKoffie/soort={soort}/name={name}")

    @GetMapping("/boons/land/{land}")

    @GetMapping("/boons/naam/{naam}")

    @GetMapping("/gerechten/koffiedrank/{koffieDrankId}")

    @GetMapping("/gerechten/afkomst/{afkomst}")


}
