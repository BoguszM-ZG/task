package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieKidControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";

    @BeforeAll
    static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/movies/kids";
    }

    @Test
    void testGetMoviesForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
        }
    }

    @Test
    void testGetMovieByIdKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        assertNotNull(response.getBody());
        var movie = response.getBody()[0];

        ResponseEntity<Object> movieResponse = restTemplate.getForEntity(baseUrl + "/" + movie.getId(), Object.class);
        assertEquals(HttpStatus.OK, movieResponse.getStatusCode());
    }

    @Test
    void testGetMovieByIdKidsNotFound() {
        ResponseEntity<Object> response = restTemplate.getForEntity(baseUrl + "/99999", Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movie not found", response.getBody());
    }

    @Test
    void testFindByCategoryForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/category/Drama", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
            assertEquals("Drama", m.getCategory());
        }

    }

    @Test
    void testGetMoviesByTitleForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findByTitle?title=test", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
            assertTrue(m.getTitle().toLowerCase().contains("test"));
        }

    }

    @Test
    void testGetNewMoviesForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findNew", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
            assertTrue(m.getTag().contains("new"));
        }

    }

    @Test
    void testGetMoviesByPremiereYearForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/premiereYear?premiereYear=1972", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
            assertEquals(1972, m.getMovie_year());
        }

    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYearForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/polishPremiere?month=09&year=1972", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
            assertEquals(9, m.getPolish_premiere().getMonthValue());
            assertEquals(1972, m.getPolish_premiere().getYear());
        }

    }

    @Test
    void testGetMoviesByWorldPremiereMonthAndYearForKids() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/worldPremiere?month=03&year=1972", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (Movie m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
            assertEquals(3, m.getWorld_premiere().getMonthValue());
            assertEquals(1972, m.getWorld_premiere().getYear());
        }

    }

    @Test
    void testGetMoviesWithAvgGradeDescForKids() {
        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/avgGradeDesc", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (MovieWithAvgGradeDto m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
        }
    }

    @Test
    void testGetMoviesWithAvgGradeAscForKids() {
        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/avgGradeAsc", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (MovieWithAvgGradeDto m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
        }
    }

    @Test
    void testGetTop10MoviesByAvgGradeForKids() {
        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/topTen", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (MovieWithAvgGradeDto m : response.getBody()) {
            assertEquals(0, m.getAgeRestriction());
        }
    }

    @Test
    void testAddKidsMovieGradeInvalidGrade() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        var movie = response.getBody()[0];

        try {
            restTemplate.postForEntity(baseUrl + "/" + movie.getId() + "/grade?grade=11", null, Double.class);
            fail("Grade must be between 1 and 10.");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void testAddKidsMovieGradeSuccess() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        Movie movie = response.getBody()[0];

        ResponseEntity<Double> gradeResponse = restTemplate.postForEntity(baseUrl + "/" + movie.getId() + "/grade?grade=8", null, Double.class);
        assertNotNull(gradeResponse.getBody());
        assertTrue(gradeResponse.getBody() >= 1 && gradeResponse.getBody() <= 10);
    }

    @Test
    void testGetKidsMovieWithAvgGrade() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        Movie movie = response.getBody()[0];


        restTemplate.postForEntity(baseUrl + "/" + movie.getId() + "/grade?grade=7", null, Double.class);

        ResponseEntity<MovieWithAvgGradeDto> avgGradeResponse = restTemplate.getForEntity(baseUrl + "/" + movie.getId() + "/details", MovieWithAvgGradeDto.class);
        assertEquals(HttpStatus.OK, avgGradeResponse.getStatusCode());
        assertNotNull(avgGradeResponse.getBody());
        assertEquals(0, avgGradeResponse.getBody().getAgeRestriction());
        assertTrue(avgGradeResponse.getBody().getAvgGrade() >= 1 && avgGradeResponse.getBody().getAvgGrade() <= 10);
    }



    @Test
    void testGetMoviePropositionsForKids() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        var movie1 = new Movie();
        movie1.setTitle("Test1");
        movie1.setCategory("Test");
        movie1.setDescription("This is a test movie description 1.");
        movie1.setMovie_year(2023);
        movie1.setPrizes("oscar");
        movie1.setAgeRestriction(0);

        var movie2 = new Movie();
        movie2.setTitle("Test2");
        movie2.setCategory("Test");
        movie2.setDescription("This is a test movie description 2.");
        movie2.setMovie_year(2023);
        movie2.setPrizes("oscar");
        movie2.setAgeRestriction(0);

        var savedMovie = restTemplate.postForEntity("http://localhost:" + port + "/movies", movie1, Movie.class);
        var savedMovie2 = restTemplate.postForEntity("http://localhost:" + port + "/movies", movie2, Movie.class);

        restTemplate.postForEntity("http://localhost:" + port + "/api/watched-movies/add/" + savedMovie.getBody().getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl + "/propositions",
                HttpMethod.GET,
                request,
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);


    }
}