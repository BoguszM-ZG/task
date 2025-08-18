package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.ReportDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.ReportComment;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ReportCommentRepository;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportCommentIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReportCommentRepository reportRepository;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/reports";
    }





    @Test
    void testReportComment() {
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

        ResponseEntity<?> addResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/comments/add/" + movieId, addRequest, Object.class);

        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        var comment = commentRepository.findAll().getFirst();

        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.set("Authorization", jwtToken);
        reportHeaders.set("Content-Type", "application/json");
        String reason = "spam";
        HttpEntity<String> reportRequest = new HttpEntity<>(reason, reportHeaders);

        ResponseEntity<?> reportResponse = restTemplate.postForEntity(
                baseUrl + "/" + comment.getId(), reportRequest, String.class);

        assertEquals(HttpStatus.OK, reportResponse.getStatusCode());
        assertEquals("spam", reportResponse.getBody());
    }

    @Test
    void testGetAllReports() {
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

        ResponseEntity<?> addResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/comments/add/" + movieId, addRequest, Object.class);

        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        var comment = commentRepository.findAll().getFirst();

        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.set("Authorization", jwtToken);
        reportHeaders.set("Content-Type", "application/json");
        String reason = "spam";
        HttpEntity<String> reportRequest = new HttpEntity<>(reason, reportHeaders);

        ResponseEntity<?> reportResponse = restTemplate.postForEntity(
                baseUrl + "/" + comment.getId(), reportRequest, String.class);

        assertEquals(HttpStatus.OK, reportResponse.getStatusCode());

        ResponseEntity<?> getReportsResponse = restTemplate.exchange(
                baseUrl, HttpMethod.GET, new HttpEntity<>(reportHeaders), Object.class);

        assertEquals(HttpStatus.OK, getReportsResponse.getStatusCode());
    }

    @Test
    void testApproveReport() {
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

        ResponseEntity<?> addResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/comments/add/" + movieId, addRequest, Object.class);

        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        var comment = commentRepository.findAll().getFirst();

        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.set("Authorization", jwtToken);
        reportHeaders.set("Content-Type", "application/json");
        String reason = "spam";
        HttpEntity<String> reportRequest = new HttpEntity<>(reason, reportHeaders);

        ResponseEntity<?> reportResponse = restTemplate.postForEntity(
                baseUrl + "/" + comment.getId(), reportRequest, String.class);

        assertEquals(HttpStatus.OK, reportResponse.getStatusCode());

        ReportComment reportComment = reportRepository.findAll().getFirst();

        ResponseEntity<?> approveResponse = restTemplate.exchange(
                baseUrl + "/approve/" + reportComment.getId(), HttpMethod.POST, new HttpEntity<>(reportHeaders), String.class);

        assertEquals(HttpStatus.OK, approveResponse.getStatusCode());
    }

    @Test
    void testRejectReport() {
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

        ResponseEntity<?> addResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/comments/add/" + movieId, addRequest, Object.class);

        assertEquals(HttpStatus.OK, addResponse.getStatusCode());

        var comment = commentRepository.findAll().getFirst();

        HttpHeaders reportHeaders = new HttpHeaders();
        reportHeaders.set("Authorization", jwtToken);
        reportHeaders.set("Content-Type", "application/json");
        String reason = "spam";
        HttpEntity<String> reportRequest = new HttpEntity<>(reason, reportHeaders);

        ResponseEntity<?> reportResponse = restTemplate.postForEntity(
                baseUrl + "/" + comment.getId(), reportRequest, String.class);

        assertEquals(HttpStatus.OK, reportResponse.getStatusCode());

        ReportComment reportComment = reportRepository.findAll().getFirst();

        ResponseEntity<?> rejectResponse = restTemplate.exchange(
                baseUrl + "/reject/" + reportComment.getId(), HttpMethod.POST, new HttpEntity<>(reportHeaders), String.class);

        assertEquals(HttpStatus.OK, rejectResponse.getStatusCode());
    }

    @Test
    void testApproveNonExistentReport() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("Content-Type", "application/json");

        try {
            restTemplate.exchange(
                    baseUrl + "/approve/9999", HttpMethod.POST, new HttpEntity<>(headers), String.class);
        }catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Report does not exist.", e.getResponseBodyAsString());
        }
    }

    @Test
    void testRejectNonExistentReport() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("Content-Type", "application/json");

        try {
            restTemplate.exchange(
                    baseUrl + "/reject/9999", HttpMethod.POST, new HttpEntity<>(headers), String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Report does not exist.", e.getResponseBodyAsString());
        }
    }

    @Test
    void testReportNonExistentComment() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("Content-Type", "application/json");
        String reason = "spam";
        HttpEntity<String> reportRequest = new HttpEntity<>(reason, headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/9999", reportRequest, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Comment does not exist", e.getResponseBodyAsString());
        }
    }

    @Test
    void testGetAllReportsWithoutReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> addRequest = new HttpEntity<>(null, headers);
        try {
            restTemplate.exchange(
                    baseUrl, HttpMethod.GET, addRequest, String.class);
        }catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.OK, e.getStatusCode());
            assertEquals("No reports found.", e.getResponseBodyAsString());
        }
    }


}
