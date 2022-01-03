package com.example.edgeservice.controller;

import com.example.edgeservice.model.*;
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

@CrossOrigin(origins = "*",maxAge = 3600)
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

    @GetMapping("/")
    public List<Koffie> homepage() {
        return getAllKoffie();
    }

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

    @GetMapping("/AlleKoffie/soort={soort}/name={name}")
    public List<KoffieFilled> getKoffieByNaamandSoort(@PathVariable String soort, @PathVariable String name) {
        List<KoffieFilled> returnlist = new ArrayList<>();

        String url = "/AlleKoffie/soort="+soort+"/name="+name;

        Koffie koffie = restTemplate.getForObject("https://" + koffieDrankenServiceBaseUrl + url,
                Koffie.class);

        ResponseEntity<List<Boon>> responseEntityBoon =
                restTemplate.exchange("https://" + koffieBonenServiceBaseUrl + "/boons/naam/{naam}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Boon>>() {
                        }, koffie.getBoonName());

        List<Boon> bonen = responseEntityBoon.getBody();

        returnlist.add(new KoffieFilled(koffie,bonen.get(0)));

        return returnlist;
    }

    @GetMapping("/boons/land/{land}")
    public List<Boon> getBoonByland(@PathVariable String land) {

        ResponseEntity<List<Boon>> responseEntityBoon =
                restTemplate.exchange("https://" + koffieBonenServiceBaseUrl + "/boons/land/{land}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Boon>>() {
                        },land);

        List<Boon> bonen = responseEntityBoon.getBody();

        return bonen;
    }

    @GetMapping("/boons/naam/{naam}")
    public List<Boon> getBoonBynaam(@PathVariable String naam) {

        ResponseEntity<List<Boon>> responseEntityBoon =
                restTemplate.exchange("https://" + koffieBonenServiceBaseUrl + "/boons/naam/{naam}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Boon>>() {
                        },naam);

        List<Boon> bonen = responseEntityBoon.getBody();

        return bonen;
    }

    @GetMapping("/gerechten/koffiedrank/{koffieDrankNaam}")
    public List<Gerechtenfilled> getGerechtByKoffieDrankId(@PathVariable String koffieDrankNaam) {
        List<Gerechtenfilled> result = new ArrayList<>();

        ResponseEntity<List<Gerecht>> responseEntityGerecht =
                restTemplate.exchange("https://" + koffieGerechtenServiceBaseUrl + "/gerechten/koffiedrank/{koffieDrankNaam}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Gerecht>>() {
                        },koffieDrankNaam);

        List<Gerecht> gerechten = responseEntityGerecht.getBody();

        for (Gerecht gerecht: gerechten) {
            ResponseEntity<List<Koffie>> responseEntityKoffie =
                    restTemplate.exchange("https://" + koffieDrankenServiceBaseUrl + "/AlleKoffie/naam={naam}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Koffie>>() {
                            }, gerecht.getKoffieDrankNaam());

            List<Koffie> koffies = responseEntityKoffie.getBody();

            result.add(new Gerechtenfilled(gerecht,koffies.get(0)));
        }

        return result;
    }

    @GetMapping("/gerechten/afkomst/{afkomst}")
    public List<Gerechtenfilled> getGerechtByafkomst(@PathVariable String afkomst) {
        List<Gerechtenfilled> result = new ArrayList<>();

        ResponseEntity<List<Gerecht>> responseEntityGerecht =
                restTemplate.exchange("https://" + koffieGerechtenServiceBaseUrl + "/gerechten/afkomst/{afkomst}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Gerecht>>() {
                        },afkomst);

        List<Gerecht> gerechten = responseEntityGerecht.getBody();

        for (Gerecht gerecht: gerechten) {
            ResponseEntity<List<Koffie>> responseEntityKoffie =
                    restTemplate.exchange("https://" + koffieDrankenServiceBaseUrl + "/AlleKoffie/naam={naam}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Koffie>>() {
                            }, gerecht.getKoffieDrankNaam());

            List<Koffie> koffies = responseEntityKoffie.getBody();

            result.add(new Gerechtenfilled(gerecht,koffies.get(0)));
        }

        return result;
    }

    @PostMapping("/gerechten")
    public Gerecht addGerecht(@RequestBody Gerecht gerecht){
        Gerecht gerecht_post =
                restTemplate.postForObject("https://" + koffieGerechtenServiceBaseUrl + "/gerechten/",
                        gerecht,Gerecht.class);

        return gerecht_post;
    }

    @PutMapping("/gerechten/naam/{naam}")
    public Gerecht updateGerecht(@PathVariable String naam ,@RequestBody Gerecht gerecht){

        ResponseEntity<Gerecht> responseEntityGerecht =
                restTemplate.exchange("https://"+koffieGerechtenServiceBaseUrl + "/gerechten/naam/{naam}",
                        HttpMethod.PUT, new HttpEntity<>(gerecht), Gerecht.class,naam);

        Gerecht retrievedGerecht = responseEntityGerecht.getBody();

        return retrievedGerecht;
    }



    // gerecht delete
}
