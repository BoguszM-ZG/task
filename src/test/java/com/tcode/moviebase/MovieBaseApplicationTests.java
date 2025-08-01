package com.tcode.moviebase;

import com.tcode.moviebase.Entities.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieBaseApplicationTests {

    @LocalServerPort
    private int port;


    private String baseUrl ="http://localhost";

    private static RestTemplate restTemplate;


    @BeforeAll
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port+"").concat("/movies");

    }

    @Test
    public void testAddMovie(){
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        Movie responce = restTemplate.postForObject(baseUrl, movie, Movie.class);


        assertEquals("Test", responce.getTitle());

    }

    @Test
    public void testGetMovieById() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        Movie response = restTemplate.getForObject(baseUrl + "/" + savedMovie.getId(), Movie.class);


        assertEquals(savedMovie.getId(), response.getId());
    }

    @Test
    public void testDeleteMovieById() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        restTemplate.delete(baseUrl + "/" + savedMovie.getId());

        try {
            restTemplate.getForEntity(baseUrl + "/" + savedMovie.getId(), Movie.class);
            fail("Expected 404 NOT FOUND after deleting movie");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetAllMovies() {
        var movie1 = new Movie();
        movie1.setTitle("Test1");
        movie1.setCategory("Test Category 1");
        movie1.setDescription("This is a test movie description 1.");
        movie1.setMovie_year(2023);
        movie1.setPrizes("oscar");

        var movie2 = new Movie();
        movie2.setTitle("Test2");
        movie2.setCategory("Test Category 2");
        movie2.setDescription("This is a test movie description 2.");
        movie2.setMovie_year(2023);
        movie2.setPrizes("oscar");

        var test1 = restTemplate.postForObject(baseUrl, movie1, Movie.class);
        var test2 = restTemplate.postForObject(baseUrl, movie2, Movie.class);



        assertEquals("Test1", test1.getTitle());
        assertEquals("Test2", test2.getTitle());
    }

    @Test
    void testGetMoviesByCategory() {
        var movie = new Movie();
        movie.setTitle("Test Category Movie");
        movie.setCategory("Action");
        movie.setDescription("This is a test movie description for Action category.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findByCategory/Action", Movie[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals("Action", response.getBody()[response.getBody().length-1].getCategory());

    }

    @Test
    void testGetMoviesByTitle(){
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Drama");
        movie.setDescription("This is a test movie description for Drama category.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findByTitle?title=Test", Movie[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertTrue(response.getBody()[response.getBody().length-1].getTitle().contains("Test"));
    }

    @Test
    void testAddGradeToMovieAndGetAvg(){
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test");
        movie.setDescription("This is a test movie description for Drama category.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");



        var savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=5", null, Double.class);
        ResponseEntity<Double> avgResponse = restTemplate.getForEntity(baseUrl + "/" + savedMovie.getId() + "/average-grade", Double.class);
        assertEquals(HttpStatus.OK, avgResponse.getStatusCode());
        assertEquals(5, avgResponse.getBody());
    }

    @Test
    void updateMovieTest() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        var savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        var movie1 = new Movie();
        movie1.setTitle("Test1");
        movie1.setCategory("Test Category");
        movie1.setDescription("This is a test movie description.");
        movie1.setMovie_year(2023);
        movie1.setPrizes("oscar");

        var movieUpdate = restTemplate.postForObject(baseUrl, movie1, Movie.class);


        restTemplate.put(baseUrl + "/" + savedMovie.getId(), movieUpdate);

        Movie updatedMovie = restTemplate.getForObject(baseUrl + "/" + savedMovie.getId(), Movie.class);
        assertEquals("Test1", updatedMovie.getTitle());
    }

    @Test
    void testGetAverageGradeNotFound() {

        Long nonExistentMovieId = 999L;


        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentMovieId + "/average-grade", Double.class);
            fail("Expected 404 NOT FOUND for average grade of non-graded movie");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }











}
