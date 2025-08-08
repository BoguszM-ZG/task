package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;

    @BeforeAll
    static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/actors";
    }

    @Test
    void testGetAllActors() {
        ResponseEntity<Actor[]> response = restTemplate.getForEntity(baseUrl, Actor[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testAddActorAndGetActorById() {
        var actor = new Actor();
        actor.setGender("Male");
        actor.setFirstName("Johnny");
        actor.setLastName("test");
        actor.setAge(18);
        actor.setDateOfBirth(LocalDate.of(2005, 1, 1));
        actor.setPlaceOfBirth("test");
        actor.setHeight(180);
        actor.setBiography("test biography");
        Actor savedActor = restTemplate.postForObject(baseUrl, actor, Actor.class);
        ResponseEntity<Actor> response = restTemplate.getForEntity(baseUrl + "/" + savedActor.getId(), Actor.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedActor.getId(), response.getBody().getId());
    }

    @Test
    void testDeleteActorById() {
        var actor = new Actor();
        actor.setGender("Male");
        actor.setFirstName("Johnny");
        actor.setLastName("test");
        actor.setAge(18);
        actor.setDateOfBirth(LocalDate.of(2005, 1, 1));
        actor.setPlaceOfBirth("test");
        actor.setHeight(180);
        actor.setBiography("test biography");

        var savedActor = restTemplate.postForObject(baseUrl, actor, Actor.class);
        restTemplate.delete(baseUrl + "/" + savedActor.getId());

        try {
            restTemplate.getForEntity(baseUrl + "/" + savedActor.getId(), Actor.class);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetActorAvgGrade() {
        var actor = new Actor();
        actor.setGender("Male");
        actor.setFirstName("Johnny");
        actor.setLastName("test");
        actor.setAge(18);
        actor.setDateOfBirth(LocalDate.of(2005, 1, 1));
        actor.setPlaceOfBirth("test");
        actor.setHeight(180);
        actor.setBiography("test biography");

        var savedActor = restTemplate.postForObject(baseUrl, actor, Actor.class);

        restTemplate.postForEntity(baseUrl + "/" + savedActor.getId() + "/grade?grade=5", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedActor.getId() + "/grade?grade=4", null, Double.class);

        ResponseEntity<Double> response = restTemplate.getForEntity(baseUrl + "/" + savedActor.getId() + "/average-grade", Double.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4.5, response.getBody());
    }

    @Test
    void testUpdateActor() {
        var oldActor = new Actor();
        oldActor.setGender("Male");
        oldActor.setFirstName("Johnny");
        oldActor.setLastName("test");
        oldActor.setAge(18);
        oldActor.setDateOfBirth(LocalDate.of(2005, 1, 1));
        oldActor.setPlaceOfBirth("test");
        oldActor.setHeight(180);
        oldActor.setBiography("test biography");

        var savedActor = restTemplate.postForObject(baseUrl, oldActor, Actor.class);
        var updateActor = new Actor();
        updateActor.setGender("Male");
        updateActor.setFirstName("test");
        updateActor.setLastName("test");
        updateActor.setAge(18);
        updateActor.setDateOfBirth(LocalDate.of(2005, 1, 1));
        updateActor.setPlaceOfBirth("test");
        updateActor.setHeight(180);
        updateActor.setBiography("test biography");

        restTemplate.put(baseUrl + "/" + savedActor.getId(), updateActor);

        var updatedActor = restTemplate.getForObject(baseUrl + "/" + savedActor.getId(), Actor.class);
        assertEquals("test", updatedActor.getFirstName());
    }

    @Test
    void testGetAvgGradeForNonExistentActor() {
        Long nonExistentActorId = 999L;
        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentActorId + "/average-grade", Double.class);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddGradeToNonExistentActor() {
        Long nonExistentActorId = 999L;
        int grade = 5;
        try {
            restTemplate.postForEntity(baseUrl + "/" + nonExistentActorId + "/grade?grade=" + grade, null, Double.class);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testUpdateNonExistentActor() {
        Long nonExistentActorId = 999L;
        var a1 = new Actor();
        a1.setGender("Male");
        a1.setFirstName("test");
        a1.setLastName("test");
        a1.setAge(18);
        a1.setDateOfBirth(LocalDate.of(2005, 1, 1));
        a1.setPlaceOfBirth("test");
        a1.setHeight(180);
        a1.setBiography("test biography");
        try {
            restTemplate.put(baseUrl + "/" + nonExistentActorId, a1);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddGradeToActorWithInvalidGrade() {
        var actor = new Actor();
        actor.setGender("Male");
        actor.setFirstName("Johnny");
        actor.setLastName("test");
        actor.setAge(18);
        actor.setDateOfBirth(LocalDate.of(2005, 1, 1));
        actor.setPlaceOfBirth("test");
        actor.setHeight(180);
        actor.setBiography("test biography");

        var savedActor = restTemplate.postForObject(baseUrl, actor, Actor.class);

        try {
            restTemplate.postForEntity(baseUrl + "/" + savedActor.getId() + "/grade?grade=11", null, Double.class);
            fail("Should throw 400");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void testDeleteNonExistentActor() {
        Long nonExistentActorId = 999L;
        try {
            restTemplate.delete(baseUrl + "/" + nonExistentActorId);
            fail("Should throw 404");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}