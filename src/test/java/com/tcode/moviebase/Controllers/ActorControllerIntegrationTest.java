package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.ActorDto;
import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Security.TestSecurityConfig;
import com.tcode.moviebase.Repositories.ActorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ActorRepository actorRepository;

    static class PageResponse<T> {
        private List<T> content;
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/actors";
        actorRepository.deleteAll();
    }

    private Actor newActor() {
        var a = new Actor();
        a.setGender("Male");
        a.setFirstName("Johnny");
        a.setLastName("Test");
        a.setAge(18);
        a.setDateOfBirth(LocalDate.of(2005,1,1));
        a.setPlaceOfBirth("Test");
        a.setHeight(180);
        a.setBiography("bio");
        return a;
    }

    @Test
    void testGetAllActors() {
        actorRepository.save(newActor());

        ResponseEntity<PageResponse<ActorDto>> resp = restTemplate.exchange(
                baseUrl + "?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertFalse(resp.getBody().getContent().isEmpty());
    }

    @Test
    void testAddActorAndGetActorById() {
        restTemplate.postForObject(baseUrl, newActor(), Object.class);

        Long id = actorRepository.findAll().stream()
                .filter(a -> a.getFirstName().equals("Johnny") && a.getLastName().equals("Test"))
                .map(Actor::getId)
                .findFirst()
                .orElseThrow();

        ResponseEntity<ActorDto> resp =
                restTemplate.getForEntity(baseUrl + "/" + id, ActorDto.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Johnny", resp.getBody().getFirstName());
        assertEquals("Test", resp.getBody().getLastName());
    }

    @Test
    void testDeleteActorById() {
        Long id = actorRepository.save(newActor()).getId();

        restTemplate.delete(baseUrl + "/" + id);

        try {
            restTemplate.getForEntity(baseUrl + "/" + id, ActorDto.class);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetActorAvgGrade() {
        Long id = actorRepository.save(newActor()).getId();

        restTemplate.postForEntity(baseUrl + "/" + id + "/grade?grade=5", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + id + "/grade?grade=4", null, Double.class);

        ResponseEntity<Double> resp =
                restTemplate.getForEntity(baseUrl + "/" + id + "/average-grade", Double.class);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(4.5, resp.getBody(), 0.0001);
    }

    @Test
    void testUpdateActor() {
        Long id = actorRepository.save(newActor()).getId();

        var update = new Actor();
        update.setGender("Male");
        update.setFirstName("Updated");
        update.setLastName("Test");
        update.setAge(19);
        update.setDateOfBirth(LocalDate.of(2005,1,1));
        update.setPlaceOfBirth("Test");
        update.setHeight(181);
        update.setBiography("updated bio");

        restTemplate.put(baseUrl + "/" + id, update);

        ActorDto after = restTemplate.getForObject(baseUrl + "/" + id, ActorDto.class);
        assertNotNull(after);
        assertEquals("Updated", after.getFirstName());
        assertEquals(19, after.getAge());
    }

    @Test
    void testGetAvgGradeForNonExistentActor() {
        Long id = 999L;
        try {
            restTemplate.getForEntity(baseUrl + "/" + id + "/average-grade", Double.class);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddGradeToNonExistentActor() {
        Long id = 999L;
        try {
            restTemplate.postForEntity(baseUrl + "/" + id + "/grade?grade=5", null, Double.class);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testUpdateNonExistentActor() {
        Long id = 999L;
        try {
            restTemplate.put(baseUrl + "/" + id, newActor());
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddGradeToActorWithInvalidGrade() {
        Long id = actorRepository.save(newActor()).getId();
        try {
            restTemplate.postForEntity(baseUrl + "/" + id + "/grade?grade=11", null, Double.class);
            fail("Should throw 400");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void testDeleteNonExistentActor() {
        Long id = 999L;
        try {
            restTemplate.delete(baseUrl + "/" + id);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}