package com.example.edgeservice;

import com.example.edgeservice.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KoffeeControllerUnitTests {

    @Value("${koffiedrankenservice.baseurl}")
    private String koffieDrankenServiceBaseUrl;

    @Value(("${koffiebonenservice.baseurl}"))
    private String koffieBonenServiceBaseUrl;

    @Value(("${koffiegerechtenservice.baseurl}"))
    private String koffieGerechtenServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private Boon boon1 = new Boon("boon1", "Belgie", "1");
    private Boon boon2 = new Boon("boon2", "Nederland", "2");

    private Koffie koffie1 = new Koffie(1l,"koffie1", "Belgie", "1990", "melk", "Koffie", "PureVorm", "boon1");
    private Koffie koffie2 = new Koffie(2l,"koffie2", "Nederland", "2000", "melk", "Koffie", "PureVorm", "boon2");

    private Gerecht gerecht1 = new Gerecht("koffie1", "gerecht1", "Belgie", 110.0, false, false, false, 4, "mock.url.be");
    private Gerecht gerecht2 = new Gerecht("koffie2", "gerecht2", "Duitsland", 100.0, true, true, true, 2, "mock1.url.be");

    private Gerechtenfilled gerechtFilled1 = new Gerechtenfilled(gerecht1, koffie1);
    private Gerechtenfilled gerechtenfilled2 = new Gerechtenfilled(gerecht2, koffie2);

    private KoffieFilled koffieFilled1 = new KoffieFilled(koffie1, boon1);
    private KoffieFilled koffieFilled2 = new KoffieFilled(koffie2, boon2);

    @BeforeEach
    public void intitializeMockserver(){
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void whenGetAlleKoffie_thenReturnKoffiesJson() throws Exception{
        List<Koffie> koffies = new ArrayList<>();
        koffies.add(koffie1);
        koffies.add(koffie2);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieDrankenServiceBaseUrl+"/AlleKoffie")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(koffies)));

        mockMvc.perform(get("/AlleKoffie"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("koffie1")))
                .andExpect(jsonPath("$[0].meaning", is("Koffie")))
                .andExpect(jsonPath("$[0].soort", is("PureVorm")))
                .andExpect(jsonPath("$[0].boonName", is("boon1")))
                .andExpect(jsonPath("$[1].where_made", is("Nederland")))
                .andExpect(jsonPath("$[1].when_made", is("2000")))
                .andExpect(jsonPath("$[1].importants_ingredient", is("melk")));
    }

    @Test
    public void whenGetAlleKoffieByName_thenReturnKoffieFilledJson() throws Exception{
        List<Boon> bonenList = new ArrayList<>();
        bonenList.add(boon1);

        List<Koffie> koffieList = new ArrayList<>();
        koffieList.add(koffie1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieDrankenServiceBaseUrl+"/AlleKoffie/naam=koffie1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(koffieList)));

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieBonenServiceBaseUrl+"/boons/naam/boon1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bonenList)));

        mockMvc.perform(get("/AlleKoffie/naam=koffie1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].koffie.name", is("koffie1")))
                .andExpect(jsonPath("$[0].koffie.meaning", is("Koffie")))
                .andExpect(jsonPath("$[0].koffie.soort", is("PureVorm")))
                .andExpect(jsonPath("$[0].koffie.boonName", is("boon1")))
                .andExpect(jsonPath("$[0].koffie.where_made", is("Belgie")))
                .andExpect(jsonPath("$[0].koffie.when_made", is("1990")))
                .andExpect(jsonPath("$[0].koffie.importants_ingredient", is("melk")))
                .andExpect(jsonPath("$[0].boon.id", is(0)))
                .andExpect(jsonPath("$[0].boon.naam", is("boon1")))
                .andExpect(jsonPath("$[0].boon.land", is("Belgie")))
                .andExpect(jsonPath("$[0].boon.uid", is("1")));
    }

    @Test
    public void whenGetAlleKoffieBySoortAndName_thenReturnKoffieFilledJson() throws Exception{
        List<Boon> bonenList = new ArrayList<>();
        bonenList.add(boon1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieDrankenServiceBaseUrl+"/AlleKoffie/soort=PureVorm/name=koffie1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(koffie1)));

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieBonenServiceBaseUrl+"/boons/naam/boon1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bonenList)));

        mockMvc.perform(get("/AlleKoffie/soort=PureVorm/name=koffie1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].koffie.name", is("koffie1")))
                .andExpect(jsonPath("$[0].koffie.meaning", is("Koffie")))
                .andExpect(jsonPath("$[0].koffie.soort", is("PureVorm")))
                .andExpect(jsonPath("$[0].koffie.boonName", is("boon1")))
                .andExpect(jsonPath("$[0].koffie.where_made", is("Belgie")))
                .andExpect(jsonPath("$[0].koffie.when_made", is("1990")))
                .andExpect(jsonPath("$[0].koffie.importants_ingredient", is("melk")))
                .andExpect(jsonPath("$[0].boon.id", is(0)))
                .andExpect(jsonPath("$[0].boon.naam", is("boon1")))
                .andExpect(jsonPath("$[0].boon.land", is("Belgie")))
                .andExpect(jsonPath("$[0].boon.uid", is("1")));
    }

    @Test
    public void whenGetAlleBoonsByLand_thenReturnBoonsJson() throws Exception{
        List<Boon> bonenList = new ArrayList<>();
        bonenList.add(boon1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieBonenServiceBaseUrl+"/boons/land/Belgie")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bonenList)));

        mockMvc.perform(get("/boons/land/Belgie"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].naam", is("boon1")))
                .andExpect(jsonPath("$[0].land", is("Belgie")))
                .andExpect(jsonPath("$[0].uid", is("1")));
    }

    @Test
    public void whenGetAlleBoonsByNaam_thenReturnBoonsJson() throws Exception{
        List<Boon> bonenList = new ArrayList<>();
        bonenList.add(boon1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieBonenServiceBaseUrl+"/boons/naam/boon1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(bonenList)));

        mockMvc.perform(get("/boons/naam/boon1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].naam", is("boon1")))
                .andExpect(jsonPath("$[0].land", is("Belgie")))
                .andExpect(jsonPath("$[0].uid", is("1")));
    }

    @Test
    public void whenGetGerechtenByAfkomst_thenReturnGerechtenJson() throws Exception{
        List<Gerecht> gerechtList = new ArrayList<>();
        gerechtList.add(gerecht1);

        List<Koffie> koffieList = new ArrayList<>();
        koffieList.add(koffie1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieGerechtenServiceBaseUrl+"/gerechten/afkomst/Belgie")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gerechtList)));

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieDrankenServiceBaseUrl+"/AlleKoffie/naam=koffie1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(koffieList)));

        mockMvc.perform(get("/gerechten/afkomst/Belgie"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].gerecht.koffieDrankNaam", is("koffie1")))
                .andExpect(jsonPath("$[0].gerecht.naam", is("gerecht1")))
                .andExpect(jsonPath("$[0].gerecht.afkomst", is("Belgie")))
                .andExpect(jsonPath("$[0].gerecht.kcal", is(110.0)))
                .andExpect(jsonPath("$[0].gerecht.aantalPersonen", is(4)))
                .andExpect(jsonPath("$[0].gerecht.url", is("mock.url.be")))
                .andExpect(jsonPath("$[0].gerecht.vegetarisch", is(false)))
                .andExpect(jsonPath("$[0].gerecht.glutenvrij", is(false)))
                .andExpect(jsonPath("$[0].gerecht.vegan", is(false)))
                .andExpect(jsonPath("$[0].koffie.name", is("koffie1")))
                .andExpect(jsonPath("$[0].koffie.meaning", is("Koffie")))
                .andExpect(jsonPath("$[0].koffie.soort", is("PureVorm")))
                .andExpect(jsonPath("$[0].koffie.boonName", is("boon1")))
                .andExpect(jsonPath("$[0].koffie.where_made", is("Belgie")))
                .andExpect(jsonPath("$[0].koffie.when_made", is("1990")))
                .andExpect(jsonPath("$[0].koffie.importants_ingredient", is("melk")));
    }

    @Test
    public void whenGetGerechtenByKoffieDrank_thenReturnGerechtenJson() throws Exception{
        List<Gerecht> gerechtList = new ArrayList<>();
        gerechtList.add(gerecht1);

        List<Koffie> koffieList = new ArrayList<>();
        koffieList.add(koffie1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieGerechtenServiceBaseUrl+"/gerechten/koffiedrank/koffie1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gerechtList)));

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://"+koffieDrankenServiceBaseUrl+"/AlleKoffie/naam=koffie1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(koffieList)));

        mockMvc.perform(get("/gerechten/koffiedrank/koffie1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].gerecht.koffieDrankNaam", is("koffie1")))
                .andExpect(jsonPath("$[0].gerecht.naam", is("gerecht1")))
                .andExpect(jsonPath("$[0].gerecht.afkomst", is("Belgie")))
                .andExpect(jsonPath("$[0].gerecht.kcal", is(110.0)))
                .andExpect(jsonPath("$[0].gerecht.aantalPersonen", is(4)))
                .andExpect(jsonPath("$[0].gerecht.url", is("mock.url.be")))
                .andExpect(jsonPath("$[0].gerecht.vegetarisch", is(false)))
                .andExpect(jsonPath("$[0].gerecht.glutenvrij", is(false)))
                .andExpect(jsonPath("$[0].gerecht.vegan", is(false)))
                .andExpect(jsonPath("$[0].koffie.name", is("koffie1")))
                .andExpect(jsonPath("$[0].koffie.meaning", is("Koffie")))
                .andExpect(jsonPath("$[0].koffie.soort", is("PureVorm")))
                .andExpect(jsonPath("$[0].koffie.boonName", is("boon1")))
                .andExpect(jsonPath("$[0].koffie.where_made", is("Belgie")))
                .andExpect(jsonPath("$[0].koffie.when_made", is("1990")))
                .andExpect(jsonPath("$[0].koffie.importants_ingredient", is("melk")));
    }

    @Test
    public void whenAddGerecht_thenReturnGerechtJson() throws Exception{
        Gerecht gerecht = new Gerecht("koffie1", "gerecht10", "Duitsland", 120.0, true, false, false, 8, "mock123.url.be");

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://" + koffieGerechtenServiceBaseUrl + "/gerechten/")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gerecht))
                );

        mockMvc.perform(post("/gerechten")
                .content(mapper.writeValueAsString(gerecht))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.koffieDrankNaam", is("koffie1")))
                .andExpect(jsonPath("$.naam", is("gerecht10")))
                .andExpect(jsonPath("$.afkomst", is("Duitsland")))
                .andExpect(jsonPath("$.kcal", is(120.0)))
                .andExpect(jsonPath("$.aantalPersonen", is(8)))
                .andExpect(jsonPath("$.url", is("mock123.url.be")))
                .andExpect(jsonPath("$.vegetarisch", is(false)))
                .andExpect(jsonPath("$.glutenvrij", is(true)))
                .andExpect(jsonPath("$.vegan", is(false)));
    }

    @Test
    public void whenUpdateGerecht_thenReturnGerechtJson() throws Exception{
        Gerecht updatedGerecht = new Gerecht("koffie1", "gerecht10", "Duitsland", 120.0, true, false, false, 8, "mock123.url.be");

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://" + koffieGerechtenServiceBaseUrl + "/gerechten/naam/gerecht1")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updatedGerecht))
                );

        mockMvc.perform(put("/gerechten/naam/gerecht1")
                .content(mapper.writeValueAsString(updatedGerecht))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.koffieDrankNaam", is("koffie1")))
                .andExpect(jsonPath("$.naam", is("gerecht10")))
                .andExpect(jsonPath("$.afkomst", is("Duitsland")))
                .andExpect(jsonPath("$.kcal", is(120.0)))
                .andExpect(jsonPath("$.aantalPersonen", is(8)))
                .andExpect(jsonPath("$.url", is("mock123.url.be")))
                .andExpect(jsonPath("$.vegetarisch", is(false)))
                .andExpect(jsonPath("$.glutenvrij", is(true)))
                .andExpect(jsonPath("$.vegan", is(false)));
    }

    @Test
    public void whenDeleteGerecht_thenReturnStatusOk() throws Exception{
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("https://" + koffieGerechtenServiceBaseUrl + "/gerechten/naam/gerecht999")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/gerechten/naam/gerecht999"))
                .andExpect(status().isOk());
    }
}
