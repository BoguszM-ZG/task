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

import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieJuniorControllerIntegrationTest {

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
        baseUrl = "http://localhost:" + port + "/movies/juniors";
    }

    @Test
    void testGetMoviesForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
        }
    }

    @Test
    void testGetMovieByIdJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        assertNotNull(response.getBody());
        var movie = response.getBody()[0];

        ResponseEntity<Object> movieResponse = restTemplate.getForEntity(baseUrl + "/" + movie.getId(), Object.class);
        assertEquals(HttpStatus.OK, movieResponse.getStatusCode());
        assertNotNull(movieResponse.getBody());
    }

    @Test
    void testGetMovieByIdJuniorsNotFound() {
        ResponseEntity<Object> response = restTemplate.getForEntity(baseUrl + "/99999", Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movie not found", response.getBody());
    }

    @Test
    void testFindByCategoryForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/category/Drama", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
            assertEquals("Drama", m.getCategory());
        }
    }

    @Test
    void testGetMoviesByTitleForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findByTitle?title=test", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
            assertTrue(m.getTitle().toLowerCase().contains("test"));
        }
    }

    @Test
    void testGetNewMoviesForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findNew", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
            assertTrue(m.getTag().contains("new"));
        }
    }

    @Test
    void testGetMoviesByPremiereYearForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/premiereYear?premiereYear=1972", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
            assertEquals(1972, m.getMovie_year());
        }
    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYearForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/polishPremiere?month=09&year=1972", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
            assertEquals(9, m.getPolish_premiere().getMonthValue());
            assertEquals(1972, m.getPolish_premiere().getYear());
        }
    }

    @Test
    void testGetMoviesByWorldPremiereMonthAndYearForJuniors() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/worldPremiere?month=03&year=1972", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (Movie m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
            assertEquals(3, m.getWorld_premiere().getMonthValue());
            assertEquals(1972, m.getWorld_premiere().getYear());
        }
    }

    @Test
    void testGetMoviesWithAvgGradeDescForJuniors() {
        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/avgGradeDesc", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (MovieWithAvgGradeDto m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
        }
    }

    @Test
    void testGetMoviesWithAvgGradeAscForJuniors() {
        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/avgGradeAsc", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (MovieWithAvgGradeDto m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
        }
    }

    @Test
    void testGetTop10MoviesByAvgGradeForJuniors() {
        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/topTen", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        for (MovieWithAvgGradeDto m : response.getBody()) {
            assertTrue(m.getAgeRestriction() <= 17);
        }
    }

    @Test
    void testAddJuniorsMovieGradeInvalidGrade() {
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
    void testAddJuniorsMovieGradeSuccess() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        Movie movie = response.getBody()[0];

        ResponseEntity<Double> gradeResponse = restTemplate.postForEntity(baseUrl + "/" + movie.getId() + "/grade?grade=8", null, Double.class);
        assertNotNull(gradeResponse.getBody());
        assertTrue(gradeResponse.getBody() >= 1 && gradeResponse.getBody() <= 10);
    }

    @Test
    void testGetJuniorsMovieWithAvgGrade() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        Movie movie = response.getBody()[0];

        restTemplate.postForEntity(baseUrl + "/" + movie.getId() + "/grade?grade=7", null, Double.class);

        ResponseEntity<MovieWithAvgGradeDto> avgGradeResponse = restTemplate.getForEntity(baseUrl + "/" + movie.getId() + "/details", MovieWithAvgGradeDto.class);
        assertEquals(HttpStatus.OK, avgGradeResponse.getStatusCode());
        assertNotNull(avgGradeResponse.getBody());
        assertTrue(avgGradeResponse.getBody().getAgeRestriction() <= 17);
        assertTrue(avgGradeResponse.getBody().getAvgGrade() >= 1 && avgGradeResponse.getBody().getAvgGrade() <= 10);
    }
}