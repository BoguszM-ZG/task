package com.tcode.moviebase;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        Movie response = restTemplate.postForObject(baseUrl, movie, Movie.class);


        assertEquals("Test", response.getTitle());

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

        restTemplate.postForObject(baseUrl, movie1, Movie.class);
        restTemplate.postForObject(baseUrl, movie2, Movie.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl, Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
        assertTrue(response.getBody()[response.getBody().length-2].getTitle().contains("Test1"));
        assertTrue(response.getBody()[response.getBody().length-1].getTitle().contains("Test2"));


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

    @Test
    void testGetMovieByIdWithAverageGrade() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=5", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=4", null, Double.class);

        MovieWithAvgGradeDto response = restTemplate.getForObject(baseUrl + "/" + savedMovie.getId()+ "/details", MovieWithAvgGradeDto.class);

        assertNotNull(response);
        assertEquals(4.5, response.getAvgGrade());
    }

    @Test
    void testGetMovieWithAvgGradeNotFound() {
        Long nonExistentMovieId = 999L;

        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentMovieId + "/details", MovieWithAvgGradeDto.class);
            fail("Expected 404 NOT FOUND for movie with average grade");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetMovieWithAvgGradeWithoutGrades() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        MovieWithAvgGradeDto response = restTemplate.getForObject(baseUrl + "/" + savedMovie.getId() + "/details", MovieWithAvgGradeDto.class);

        assertNotNull(response);
        assertEquals(savedMovie.getTitle(), response.getTitle());
        assertEquals(savedMovie.getCategory(), response.getCategory());
        assertEquals(savedMovie.getDescription(), response.getDescription());
        assertEquals(savedMovie.getMovie_year(), response.getMovie_year());
        assertEquals(savedMovie.getPrizes(), response.getPrizes());
        assertNull(response.getAvgGrade());
    }

    @Test
    void testGetMovieWithAvgGradeForNonExistentMovie() {
        Long nonExistentMovieId = 999L;

        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentMovieId + "/details", MovieWithAvgGradeDto.class);
            fail("Expected 404 NOT FOUND for movie with average grade");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testUpdateMovieNotFound() {
        Long nonExistentMovieId = 999L;
        var movie = new Movie();
        movie.setTitle("Updated Title");
        movie.setCategory("Updated Category");
        movie.setDescription("Updated Description");
        movie.setMovie_year(2023);
        movie.setPrizes("Updated Prizes");

        try {
            restTemplate.put(baseUrl + "/" + nonExistentMovieId, movie);
            fail("Expected 404 NOT FOUND for updating non-existent movie");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddGradeToNonExistentMovie() {
        Long nonExistentMovieId = 999L;
        int grade = 5;

        try {
            restTemplate.postForEntity(baseUrl + "/" + nonExistentMovieId + "/grade?grade=" + grade, null, Double.class);
            fail("Expected 404 NOT FOUND for adding grade to non-existent movie");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetMovieWithNonExistentId() {
        Long nonExistentMovieId = 999L;

        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentMovieId, Movie.class);
            fail("Expected 404 NOT FOUND for non-existent movie");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetMoviesByTitleNoContent() {
        String title = "NonExistentTitle";

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findByTitle?title=" + title, Movie[].class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetMoviesByCategoryNoContent() {
        String category = "NonExistentCategory";

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findByCategory/" + category, Movie[].class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteNonExistentMovie() {
        Long nonExistentMovieId = 999L;

        try {
            restTemplate.delete(baseUrl + "/" + nonExistentMovieId);
            fail("Expected 404 NOT FOUND for deleting non-existent movie");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddInvalidGrade() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        try {
            restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=11", null, Double.class);
            fail("Expected 400 BAD REQUEST for invalid grade");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertEquals("Grade must be between 1 and 10.", e.getResponseBodyAsString());
        }
    }

    @Test
    void testGetMoviesWithTag() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setTag("new");

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/findNew", Movie[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals(savedMovie.getId(), response.getBody()[response.getBody().length-1].getId());
    }

    @Test
    void testGetMoviesByPremiereYear() {
        int premiereYear = 2023;

        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(premiereYear);
        movie.setPrizes("oscar");

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/premiereYear?premiereYear=" + premiereYear, Movie[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals(premiereYear, response.getBody()[response.getBody().length-1].getMovie_year());
    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYear() {
        int month = 5;
        int year = 2023;

        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setPolish_premiere(java.time.LocalDate.of(year, month, 15));

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/polishPremiereMonthAndYear?month=" + month + "&year=" + year, Movie[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals(month, response.getBody()[response.getBody().length-1].getPolish_premiere().getMonthValue());
        assertEquals(year, response.getBody()[response.getBody().length-1].getPolish_premiere().getYear());
    }

    @Test
    void testGetMoviesByWorldPremiereMonthAndYear() {
        int month = 6;
        int year = 2023;

        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setWorld_premiere(java.time.LocalDate.of(year, month, 20));

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/worldPremiereMonthAndYear?month=" + month + "&year=" + year, Movie[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals(month, response.getBody()[response.getBody().length-1].getWorld_premiere().getMonthValue());
        assertEquals(year, response.getBody()[response.getBody().length-1].getWorld_premiere().getYear());
    }

    @Test
    void testGetMoviesByYearNoContent() {
        int nonExistentYear = 2100;

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/premiereYear?premiereYear=" + nonExistentYear, Movie[].class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYearNoContent() {
        int month = 6;
        int year = 2100;
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/polishPremiereMonthAndYear?month=" + month + "&year=" + year, Movie[].class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetMoviesByWorldPremiereMonthAndYearNoContent() {
        int month = 6;
        int year = 2100;
        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/worldPremiereMonthAndYear?month=" + month + "&year=" + year, Movie[].class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void testGetAllMoviesSortedByAvgDesc() {
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

        var savedMovie1 = restTemplate.postForObject(baseUrl, movie1, Movie.class);
        var savedMovie2 = restTemplate.postForObject(baseUrl, movie2, Movie.class);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie1.getId() + "/grade?grade=10", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie1.getId() + "/grade?grade=9", null, Double.class);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie2.getId() + "/grade?grade=10", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie2.getId() + "/grade?grade=9", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie2.getId() + "/grade?grade=9", null, Double.class);



        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/sortedByAvgGradeDesc", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
        assertTrue(response.getBody()[2].getTitle().contains("Test1"));
        assertTrue(response.getBody()[3].getTitle().contains("Test2"));

    }

    @Test
    void testGetAllMoviesSortedByAvgAsc() {
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

        var savedMovie1 = restTemplate.postForObject(baseUrl, movie1, Movie.class);
        var savedMovie2 = restTemplate.postForObject(baseUrl, movie2, Movie.class);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie1.getId() + "/grade?grade=2", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie2.getId() + "/grade?grade=1", null, Double.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/sortedByAvgGradeAsc", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    void testGetTopTenMoviesByAvgGrade() {
        var movie1 = new Movie();
        movie1.setTitle("Test10");
        movie1.setCategory("Test Category 1");
        movie1.setDescription("This is a test movie description 1.");
        movie1.setMovie_year(2023);
        movie1.setPrizes("oscar");

        var movie2 = new Movie();
        movie2.setTitle("Test12");
        movie2.setCategory("Test Category 2");
        movie2.setDescription("This is a test movie description 2.");
        movie2.setMovie_year(2023);
        movie2.setPrizes("oscar");

        var savedMovie1 = restTemplate.postForObject(baseUrl, movie1, Movie.class);
        var savedMovie2 = restTemplate.postForObject(baseUrl, movie2, Movie.class);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie1.getId() + "/grade?grade=10", null, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie2.getId() + "/grade?grade=9", null, Double.class);

        ResponseEntity<Movie[]> response = restTemplate.getForEntity(baseUrl + "/top10ByAvgGrade", Movie[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().length);
    }


















}
