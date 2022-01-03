package com.example.edgeservice.controller;

import com.example.edgeservice.model.Boon;
import com.example.edgeservice.model.Koffie;
import com.example.edgeservice.model.KoffieFilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/AlleKoffie")
    public List<Koffie> getAllKoffie() {
        List<Koffie> returnList = new ArrayList<>();

        ResponseEntity<List<Koffie>> responseEntityKoffie =
                restTemplate.exchange("https://"+koffieDrankenServiceBaseUrl+"/AlleKoffie",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Koffie>>() {
                        });
        returnList = responseEntityKoffie.getBody();

        return returnList;
    }

    @GetMapping("/AlleKoffie/naam={naam}")
    public List<KoffieFilled> getKoffieByNaam(@PathVariable String naam){
        List<KoffieFilled> returnlist = new ArrayList<>();

        ResponseEntity<List<Koffie>> responseEntityKoffie =
                restTemplate.exchange("https://" + koffieDrankenServiceBaseUrl + "/AlleKoffie/naam={naam}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Koffie>>() {
                        }, naam);

        List<Koffie> koffies = responseEntityKoffie.getBody();

        for (Koffie koffie:koffies) {
            ResponseEntity<List<Boon>> responseEntityBoon =
                    restTemplate.exchange("https://" + koffieBonenServiceBaseUrl + "/boons/naam/{naam}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Boon>>() {
                            }, koffie.getBoonName());

            List<Boon> bonen = responseEntityBoon.getBody();

            returnlist.add(new KoffieFilled(koffie,bonen.get(0)));
        }

        return returnlist;
    }

//    /AlleKoffie/soort={soort}/name={name}
//
//    /boons/land/{land}
//
//    /boons/naam/{naam}
//
//    /gerechten/koffiedrank/{koffieDrankId}
//
//    /gerechten/afkomst/{afkomst}


}
