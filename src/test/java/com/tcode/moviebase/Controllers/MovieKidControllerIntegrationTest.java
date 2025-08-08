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
class MovieKidControllerIntegrationTest {

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
}