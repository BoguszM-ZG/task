package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.client.HttpClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private MovieRepository movieRepository;

    private static RestTemplate restTemplate;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";
    @Autowired
    private HttpClientProperties httpClientProperties;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/comments";
    }


    @Test
    void testAddAndGetComment() {
        // given
        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Long movieId = movie.getId();

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.set("Authorization", jwtToken);
        postHeaders.setContentType(MediaType.APPLICATION_JSON);

        String commentJson = "\"great movie!\"";
        ResponseEntity<?> addResp = restTemplate.postForEntity(
                baseUrl + "/add/" + movieId,
                new HttpEntity<>(commentJson, postHeaders),
                Object.class
        );
        assertEquals(HttpStatus.OK, addResp.getStatusCode());

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.set("Authorization", jwtToken);


        ResponseEntity<MovieControllerIntegrationTest.PageResponse<Comment>> getResp = restTemplate.exchange(
                baseUrl + "/movie/" + movieId + "?page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders),
                new org.springframework.core.ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, getResp.getStatusCode());
        assertNotNull(getResp.getBody());
        MovieControllerIntegrationTest.PageResponse<Comment> page = getResp.getBody();
        assertNotNull(page.getContent());
        assertFalse(page.getContent().isEmpty());
        assertTrue(page.getContent().getFirst().getCommentText().toLowerCase().contains("great"));
    }

    @Test
    void testAddComment() {
        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Long movieId = movie.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("Content-Type", "application/json");
        String commentText = "\"great movie!\"";
        HttpEntity<String> addRequest = new HttpEntity<>(commentText, headers);

        ResponseEntity<?> addResponse = restTemplate.postForEntity(
                baseUrl + "/add/" + movieId, addRequest, Object.class);

        assertEquals(HttpStatus.OK, addResponse.getStatusCode());
    }



    @Test
    void testUpdateComment() {
        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Long movieId = movie.getId();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("Content-Type", "application/json");
        String commentText = "great movie!";
        HttpEntity<String> addRequest = new HttpEntity<>(commentText, headers);
        restTemplate.postForEntity(baseUrl + "/add/" + movieId, addRequest, Object.class);


        var comment = commentRepository.findAll().getFirst();


        String updatedText = "updated comment text";
        HttpEntity<String> updateRequest = new HttpEntity<>(updatedText, headers);

        ResponseEntity<?> updateResponse = restTemplate.exchange(
                baseUrl + "/update/" + comment.getId(),
                HttpMethod.PUT,
                updateRequest,
                Object.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    }

    @Test
    void testDeleteComment() {
        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Long movieId = movie.getId();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("Content-Type", "application/json");
        String commentText = "\"great movie!\"";
        HttpEntity<String> addRequest = new HttpEntity<>(commentText, headers);
        restTemplate.postForEntity(baseUrl + "/add/" + movieId, addRequest, Object.class);


        var comment = commentRepository.findAll().getFirst();


        HttpEntity<Void> deleteRequest = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                baseUrl + "/delete/" + comment.getId(),
                HttpMethod.DELETE,
                deleteRequest,
                String.class);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Comment deleted successfully.", deleteResponse.getBody());
    }



}
