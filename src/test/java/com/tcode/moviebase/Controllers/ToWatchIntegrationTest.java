package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToWatchIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private MovieRepository movieRepository;

    private static RestTemplate restTemplate;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";
    @Autowired
    private HttpClientProperties httpClientProperties;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    static class PageResponse<T> {
        private List<T> content;
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
    }


    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/to-watch";
        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
    }

    @Test
    public void testAddAndGetToWatchMovie() {
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
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ResponseEntity<?> response = restTemplate.postForEntity(
                baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> getResponse = restTemplate.exchange(
                baseUrl,
                org.springframework.http.HttpMethod.GET,
                request,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {
                }
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddToWatchMovieNotFound() {
        Long nonExistentMovieId = 999L;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);


        try {
            restTemplate.postForEntity(
                    baseUrl + "/add/" + nonExistentMovieId, request, MovieWithAvgGradeDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testAddToWatchMovieAlreadyExists() {
        Long movieId = movieRepository.findAll().getFirst().getId();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        restTemplate.postForEntity(baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);

        try {
            restTemplate.postForEntity(baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testRemoveToWatchMovie() {
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
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        restTemplate.postForEntity(baseUrl + "/add/" + movieId, request, MovieWithAvgGradeDto.class);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/remove/" + movieId,
                org.springframework.http.HttpMethod.DELETE,
                request,
                Void.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveNonExistentToWatchMovie() {
        Long nonExistentMovieId = 999L;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        try {
            restTemplate.exchange(
                    baseUrl + "/remove/" + nonExistentMovieId,
                    org.springframework.http.HttpMethod.DELETE,
                    request,
                    Void.class);

        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testGetToWatchMoviesByCreatedAtNewest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);


        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/latest",
                org.springframework.http.HttpMethod.GET,
                request,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    public void testGetToWatchMoviesByCreatedAtOldest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);


        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/oldest",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetToWatchMoviesByCategory() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Sci-FI");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/category?category=Sci-FI",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    public void testGetToWatchMoviesByTitleZ_A() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);

        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);
        var movie2 = new Movie();
        movie2.setTitle("zest");
        movie2.setMovie_year(2023);
        movie2.setCategory("Drama");
        movie2.setDescription("A test movie for integration testing.");
        movie2.setPrizes("Best Picture");
        movie2.setAgeRestriction(0);
        movieRepository.save(movie2);

        restTemplate.postForEntity(baseUrl + "/add/" + movie2.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/sortByTitleZ_A",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetToWatchMoviesByTitleA_Z() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        var movie = new Movie();
        movie.setTitle("test");
        movie.setMovie_year(2023);
        movie.setCategory("Drama");
        movie.setDescription("A test movie for integration testing.");
        movie.setPrizes("Best Picture");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForEntity(baseUrl + "/add/" + movie.getId(), request, MovieWithAvgGradeDto.class);

        var movie2 = new Movie();
        movie2.setTitle("zest");
        movie2.setMovie_year(2023);
        movie2.setCategory("Drama");
        movie2.setDescription("A test movie for integration testing.");
        movie2.setPrizes("Best Picture");
        movie2.setAgeRestriction(0);
        movieRepository.save(movie2);

        restTemplate.postForEntity(baseUrl + "/add/" + movie2.getId(), request, MovieWithAvgGradeDto.class);

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/sortByTitleA_Z",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());


    }

    @Test
    public void testGetToWatchMoviesNoExistingCategory(){
        String nonExistentCategory = "NonExistentCategory";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.exchange(
                baseUrl + "/category?category=" + nonExistentCategory,
                HttpMethod.GET,
                request,
                MovieWithAvgGradeDto[].class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }


//    @Test
//    public void testAddToWatchMovieForKids() {
//        var movie = new Movie();
//        movie.setTitle("test");
//        movie.setMovie_year(2023);
//        movie.setCategory("Kids");
//        movie.setDescription("A test movie for integration testing.");
//        movie.setPrizes("Best Picture");
//        movie.setAgeRestriction(0);
//        movieRepository.save(movie);
//        Long movieId = movie.getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//        ResponseEntity<?> response = restTemplate.postForEntity(
//                baseUrl + "/kids/add/" + movieId, request, MovieWithAvgGradeDto.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//    @Test
//    public void testAddToWatchMovieForKidWhichIsForAdult() {
//        var movie = new Movie();
//        movie.setTitle("test");
//        movie.setMovie_year(2023);
//        movie.setCategory("Kids");
//        movie.setDescription("A test movie for integration testing.");
//        movie.setPrizes("Best Picture");
//        movie.setAgeRestriction(18);
//        movieRepository.save(movie);
//        Long movieId = movie.getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//
//        try {
//            restTemplate.postForEntity(
//                    baseUrl + "/kids/add/" + movieId, request, MovieWithAvgGradeDto.class);
//        }catch (HttpClientErrorException e){
//            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
//            assertEquals("Movie is not suitable for kids", e.getResponseBodyAsString());
//        }
//    }
//    @Test
//    public void testAddToWatchMovieAlreadyExistsForKids() {
//        var movie = new Movie();
//        movie.setTitle("test");
//        movie.setMovie_year(2023);
//        movie.setCategory("Kids");
//        movie.setDescription("A test movie for integration testing.");
//        movie.setPrizes("Best Picture");
//        movie.setAgeRestriction(0);
//        movieRepository.save(movie);
//        Long movieId = movie.getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//        restTemplate.postForEntity(baseUrl + "/kids/add/" + movieId, request, MovieWithAvgGradeDto.class);
//
//        try {
//            restTemplate.postForEntity(baseUrl + "/kids/add/" + movieId, request, MovieWithAvgGradeDto.class);
//        } catch (HttpClientErrorException e) {
//            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
//            assertEquals("Movie already in watch list", e.getResponseBodyAsString());
//        }
//    }
//
//
//    @Test
//    public void testAddToWatchMovieForJuniors() {
//        var movie = new Movie();
//        movie.setTitle("test");
//        movie.setMovie_year(2023);
//        movie.setCategory("Kids");
//        movie.setDescription("A test movie for integration testing.");
//        movie.setPrizes("Best Picture");
//        movie.setAgeRestriction(17);
//        movieRepository.save(movie);
//        Long movieId = movie.getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//        ResponseEntity<?> response = restTemplate.postForEntity(
//                baseUrl + "/juniors/add/" + movieId, request, MovieWithAvgGradeDto.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    public void testAddToWatchMovieForJuniorWhichIsForAdult() {
//        var movie = new Movie();
//        movie.setTitle("test");
//        movie.setMovie_year(2023);
//        movie.setCategory("Kids");
//        movie.setDescription("A test movie for integration testing.");
//        movie.setPrizes("Best Picture");
//        movie.setAgeRestriction(18);
//        movieRepository.save(movie);
//        Long movieId = movie.getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//
//        try {
//            restTemplate.postForEntity(
//                    baseUrl + "/juniors/add/" + movieId, request, MovieWithAvgGradeDto.class);
//        }catch (HttpClientErrorException e){
//            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
//            assertEquals("Movie is not suitable for juniors", e.getResponseBodyAsString());
//        }
//    }
//
//    @Test
//    public void testAddToWatchMovieAlreadyExistsForJuniors() {
//        var movie = new Movie();
//        movie.setTitle("test");
//        movie.setMovie_year(2023);
//        movie.setCategory("Kids");
//        movie.setDescription("A test movie for integration testing.");
//        movie.setPrizes("Best Picture");
//        movie.setAgeRestriction(0);
//        movieRepository.save(movie);
//        Long movieId = movie.getId();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//        restTemplate.postForEntity(baseUrl + "/juniors/add/" + movieId, request, MovieWithAvgGradeDto.class);
//
//        try {
//            restTemplate.postForEntity(baseUrl + "/juniors/add/" + movieId, request, MovieWithAvgGradeDto.class);
//        } catch (HttpClientErrorException e) {
//            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
//            assertEquals("Movie already in watch list", e.getResponseBodyAsString());
//        }
//    }
//
//    @Test
//    public void testAddToWatchMovieNotFoundForKids() {
//        Long nonExistentMovieId = 999L;
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//
//        try {
//            ResponseEntity<?> response = restTemplate.postForEntity(
//                    baseUrl + "/kids/add/" + nonExistentMovieId, request, MovieWithAvgGradeDto.class);
//            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//            assertEquals("Movie not found with id: " + nonExistentMovieId, response.getBody());
//        } catch (HttpClientErrorException e) {
//            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
//        }
//    }
//    @Test
//    public void testAddToWatchMovieNotFoundForJuniors() {
//        Long nonExistentMovieId = 999L;
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", jwtToken);
//        HttpEntity<Void> request = new HttpEntity<>(null, headers);
//
//
//        try {
//            ResponseEntity<?> response = restTemplate.postForEntity(
//                    baseUrl + "/juniors/add/" + nonExistentMovieId, request, MovieWithAvgGradeDto.class);
//            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//            assertEquals("Movie not found with id: " + nonExistentMovieId, response.getBody());
//        } catch (HttpClientErrorException e) {
//            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
//        }
//    }







}
