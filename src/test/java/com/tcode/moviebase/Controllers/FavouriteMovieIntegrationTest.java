package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FavouriteMovieIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private MovieRepository movieRepository;

    private static RestTemplate restTemplate;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/favourites";
        Movie movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
    }


    @Test
    void testAddFavouriteMovieAndGetFavouriteMovies() {
        Long movieId = movieRepository.findAll().getFirst().getId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(
                baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<MovieWithAvgGradeDto[]> getResponse = restTemplate.exchange(
                baseUrl,
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("test", getResponse.getBody()[0].getTitle());

    }

    @Test
    void testAddFavouriteMovieNotFound() {
        Long nonExistentMovieId = 999L;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);


        try {
            ResponseEntity<?> response = restTemplate.postForEntity(
                    baseUrl + "/add/" + nonExistentMovieId, request, MovieWithAvgGradeDto.class);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Movie not found with id: " + nonExistentMovieId, response.getBody());
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void testAddFavouriteMovieAlreadyExists() {
        Movie movie = new Movie();
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
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        restTemplate.postForEntity(
                baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Movie already in favourites", e.getResponseBodyAsString());
        }
    }

    @Test
    void testDeleteMovieFromFavourites() {
        Movie movie = new Movie();
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
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        restTemplate.postForEntity(
                baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/delete/" + movieId,
                org.springframework.http.HttpMethod.DELETE,
                request,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    @Test
    void testDeleteNonExistentMovieFromFavourites() {
        Long nonExistentMovieId = 999L;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        try {
            restTemplate.exchange(
                    baseUrl + "/delete/" + nonExistentMovieId,
                    org.springframework.http.HttpMethod.DELETE,
                    request,
                    Void.class);

        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Movie not found in favourites", e.getResponseBodyAsString());
        }
    }

    @Test
    void testGetFavouriteMoviesByCreationDateLatest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        Movie movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/latest",
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody()[0].getTitle());
    }

    @Test
    void testGetFavouriteMoviesByCreationDateOldest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        Movie movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/oldest",
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody()[0].getTitle());
    }

    @Test
    void testGetFavouriteMoviesByTitleZ_A() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        Movie movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Movie movie2 = new Movie();
        movie2.setTitle("zest");
        movie2.setMovie_year(2023);
        movie2.setCategory("Drama");
        movie2.setDescription("A test movie for integration testing.");
        movie2.setPrizes("Best Picture");
        movie2.setAgeRestriction(0);
        movieRepository.save(movie2);

        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);
        restTemplate.postForEntity(baseUrl + "/add/" + movie2.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/sortByTitleZ-A",
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("zest", response.getBody()[0].getTitle());
    }
    @Test
    void testGetFavouriteMoviesByTitleA_Z() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        Movie movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Movie movie2 = new Movie();
        movie2.setTitle("zest");
        movie2.setMovie_year(2023);
        movie2.setCategory("Drama");
        movie2.setDescription("A test movie for integration testing.");
        movie2.setPrizes("Best Picture");
        movie2.setAgeRestriction(0);
        movieRepository.save(movie2);

        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);
        restTemplate.postForEntity(baseUrl + "/add/" + movie2.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/sortByTitleA-Z",
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody()[0].getTitle());
    }

    @Test
    void testGetFavouriteMoviesByCategory() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        Movie movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Science-Fiction");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        Movie movie2 = new Movie();
        movie2.setTitle("zest");
        movie2.setMovie_year(2023);
        movie2.setCategory("Crime");
        movie2.setDescription("A test movie for integration testing.");
        movie2.setPrizes("Best Picture");
        movie2.setAgeRestriction(0);
        movieRepository.save(movie2);

        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);
        restTemplate.postForEntity(baseUrl + "/add/" + movie2.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/category?category=Science-Fiction",
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody()[0].getTitle());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void testGetFavouriteMoviesByCategoryNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/category?category=NonExistentCategory",
                org.springframework.http.HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }







}




