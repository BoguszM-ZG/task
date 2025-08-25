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
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private static RestTemplate restTemplate;

    private final String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI2MWRLaC1LVk0yN0lGSkh3WHg1Q25sZmNnckhPQWM4bWtDbjBjRzZGY0VNIn0.eyJleHAiOjE3NTQ5MTUxNTksImlhdCI6MTc1NDg5NzE1OSwianRpIjoib25ydHJvOjBhYjQ2ZGM3LWI3NDItZDhmYS0xZjUxLTM3ZGEzMjZlZTg5OSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZWFsbXMvbXlyZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJiZDIzN2Q3ZC1iNTJmLTQxYjgtYTgzYi0wMjUzMmE5MGQ3OTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteWNsaWVudCIsInNpZCI6ImQyOTk0ZDEwLTZmOGEtNGUyZS1hNWVmLWZlYTUyN2QxZmRiOSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1teXJlYWxtIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInVzZXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJteWNsaWVudCI6eyJyb2xlcyI6WyJjbGllbnRfdXNlciJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiVGVzdCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiY2xpZW50dXNlciIsImdpdmVuX25hbWUiOiJUZXN0IiwiZmFtaWx5X25hbWUiOiJUZXN0IiwiZW1haWwiOiJ0ZXN0MkB3cC5wbCJ9.gjgrJOuTquxblUO1SKyBM3ggBfm4JZUqk0UgYew62EfptPqJQhlyj_VSgRqvLBQU9big4uzOVELwnRTWkIK6zVQ7xALsHb_gcXQgUEud905GW4Mbg-mvyH6w2DU2lDxzQ6ulsyJhOsKSar-HUG6UICoRvCt9vSHkHtD3E7t2QZFfsKJy_tBhEDmfX8LqPije3mGUEfhKnIZWwI3FGIowsMl0bLfRnOIoBNmnFR4D6bFR9RP2_LQ1No0FxCXprUAropKg1DYzrJ0mSWN2M1Qp-rabKpR_exV31dD7igABX9M6O5s2VruFlpeiotTgFG8f1z37ekSWBxduFHDtRlddGA";
    @Autowired
    private MovieRepository movieRepository;


    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/movies";
    }

    @Test
    public void testAddMovie() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        Movie response = restTemplate.postForObject(baseUrl, movie, Movie.class);

        assertEquals("Test", response.getTitle());
    }

    @Test
    void testGetMovieWithAvgGradeDto() {

        Movie movie = new Movie();
        movie.setTitle("Avg Test");
        movie.setCategory("Drama");
        movie.setDescription("Desc");
        movie.setMovie_year(2024);
        movie.setPrizes("none");
        movie.setAgeRestriction(0);
        Movie saved = movieRepository.save(movie);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> authReq = new HttpEntity<>(headers);
        restTemplate.postForEntity(baseUrl + "/" + saved.getId() + "/grade?grade=8", authReq, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + saved.getId() + "/grade?grade=6", authReq, Double.class);


        MovieWithAvgGradeDto dto = restTemplate.getForObject(baseUrl + "/" + saved.getId(), MovieWithAvgGradeDto.class);


        assertNotNull(dto);
        assertEquals(saved.getTitle(), dto.getTitle());
        assertEquals(saved.getCategory(), dto.getCategory());
        assertEquals(saved.getDescription(), dto.getDescription());
        assertEquals(saved.getMovie_year(), dto.getMovie_year());
        assertNotNull(dto.getAvgGrade());
    }

    @Test
    public void testDeleteMovieById() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie, Movie.class);
        var savedMovie = movieRepository.save(movie);


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
        movie1.setAgeRestriction(0);

        var movie2 = new Movie();
        movie2.setTitle("Test2");
        movie2.setCategory("Test Category 2");
        movie2.setDescription("This is a test movie description 2.");
        movie2.setMovie_year(2023);
        movie2.setPrizes("oscar");
        movie2.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie1, Movie.class);
        restTemplate.postForObject(baseUrl, movie2, Movie.class);

        ResponseEntity<PageResponse<Movie>> response = restTemplate.exchange(
                baseUrl + "?page=0&size=50",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<Movie>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getContent().size() >= 2);

        var titles = page.getContent().stream().map(Movie::getTitle).toList();
        assertTrue(titles.contains("Test1"));
        assertTrue(titles.contains("Test2"));
    }

    @Test
    void testGetMoviesByCategory() {
        var movie = new Movie();
        movie.setTitle("Test Category Movie");
        movie.setCategory("Action");
        movie.setDescription("This is a test movie description for Action category.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<PageResponse<Movie>> response = restTemplate.exchange(
                baseUrl + "/findByCategory?category=Action",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<Movie>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertFalse(page.getContent().isEmpty());
        assertTrue(page.getContent().stream().anyMatch(m -> "Action".equals(m.getCategory())));
    }


    @Test
    void testGetMoviesByTitle() {
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Drama");
        movie.setDescription("This is a test movie description for Drama category.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        ResponseEntity<PageResponse<Movie>> response = restTemplate.exchange(
                baseUrl + "/search?title=Test",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<Movie>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getContent().size() > 0);
        assertTrue(page.getContent().stream().anyMatch(m -> m.getTitle() != null && m.getTitle().contains("Test")));
    }

    @Test
    void testAddGradeToMovieAndGetAvg() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test");
        movie.setDescription("This is a test movie description for Drama category.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);
        movieRepository.save(movie);
        restTemplate.postForObject(baseUrl, movie, Movie.class);


        restTemplate.postForEntity(baseUrl + "/" + movie.getId() + "/grade?grade=5", request, Double.class);
        ResponseEntity<Double> avgResponse = restTemplate.getForEntity(baseUrl + "/" + movie.getId() + "/average-grade", Double.class);
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
        movie.setAgeRestriction(0);
        var saved  = movieRepository.save(movie);

        restTemplate.postForObject(baseUrl, movie, Movie.class);

        var movie1 = new Movie();
        movie1.setCategory("Test");
        movie1.setDescription("This is a test movie description.");
        movie1.setPrizes("oscar");
        movie.setAgeRestriction(0);

        restTemplate.put(baseUrl + "/" + saved.getId(), movie1);

        Movie updatedMovie = restTemplate.getForObject(baseUrl + "/" + saved.getId(), Movie.class);
        assertEquals("Test", updatedMovie.getCategory());
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
    void testUpdateMovieNotFound() {
        Long nonExistentMovieId = 999L;
        var movie = new Movie();
        movie.setTitle("Updated Title");
        movie.setCategory("Updated Category");
        movie.setDescription("Updated Description");
        movie.setMovie_year(2023);
        movie.setPrizes("Updated Prizes");
        movie.setAgeRestriction(0);

        try {
            restTemplate.put(baseUrl + "/" + nonExistentMovieId, movie);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddGradeToNonExistentMovie() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        Long nonExistentMovieId = 999L;
        int grade = 5;

        try {
            restTemplate.postForEntity(baseUrl + "/" + nonExistentMovieId + "/grade?grade=" + grade, request, Double.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetMovieWithNonExistentId() {
        Long nonExistentMovieId = 999L;

        try {
            restTemplate.getForEntity(baseUrl + "/" + nonExistentMovieId, MovieWithAvgGradeDto.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testGetMoviesByTitleNoContent() {
        String title = "NonExistentTitle";

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/search?title=" + title, MovieWithAvgGradeDto[].class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }



    @Test
    void testDeleteNonExistentMovie() {
        Long nonExistentMovieId = 999L;

        try {
            restTemplate.delete(baseUrl + "/" + nonExistentMovieId);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void testAddInvalidGrade() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        Movie savedMovie = restTemplate.postForObject(baseUrl, movie, Movie.class);

        try {
            restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=11", request, Double.class);
            fail("Expected 400 BAD REQUEST for invalid grade");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
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
    void testGetTopTenMoviesByAvgGrade() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        var movie1 = new Movie();
        movie1.setTitle("Test10");
        movie1.setCategory("Test Category 1");
        movie1.setDescription("This is a test movie description 1.");
        movie1.setMovie_year(2023);
        movie1.setPrizes("oscar");
        movie1.setAgeRestriction(0);

        var movie2 = new Movie();
        movie2.setTitle("Test12");
        movie2.setCategory("Test Category 2");
        movie2.setDescription("This is a test movie description 2.");
        movie2.setMovie_year(2023);
        movie2.setPrizes("oscar");
        movie2.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie1, Movie.class);
        var savedMovie = movieRepository.save(movie1);
        restTemplate.postForObject(baseUrl, movie2, Movie.class);
        var savedMovie2 = movieRepository.save(movie2);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=10", request, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie2.getId() + "/grade?grade=9", request, Double.class);

        ResponseEntity<MovieWithAvgGradeDto[]> response = restTemplate.getForEntity(baseUrl + "/top10ByAvgGrade", MovieWithAvgGradeDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }






    @Test
    void testGetAvgGradeByUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie, Movie.class);
        var savedMovie = movieRepository.save(movie);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=5", request, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=4", request, Double.class);

        ResponseEntity<Double> response = restTemplate.exchange(
                baseUrl + "/average-grade-by-user",
                HttpMethod.GET,
                request,
                Double.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() >= 1.0 && response.getBody() <= 10.0);
    }


    @Test
    void testGetAvgGradeByUserIdInYearAndMonth() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        var movie = new Movie();
        movie.setTitle("Test");
        movie.setCategory("Test Category");
        movie.setDescription("This is a test movie description.");
        movie.setMovie_year(2023);
        movie.setPrizes("oscar");
        movie.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie, Movie.class);
        var savedMovie = movieRepository.save(movie);

        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=5", request, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + savedMovie.getId() + "/grade?grade=4", request, Double.class);

        ResponseEntity<Double> response = restTemplate.exchange(
                baseUrl + "/avg-grade-by-user-y-m?year=2025&month=08",
                HttpMethod.GET,
                request,
                Double.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    void testGetNewMovies() {
        var newMovie = new Movie();
        newMovie.setTitle("Brand New");
        newMovie.setCategory("News");
        newMovie.setDescription("New movie");
        newMovie.setMovie_year(2024);
        newMovie.setPrizes("none");
        newMovie.setAgeRestriction(0);
        newMovie.setTag("new");

        var oldMovie = new Movie();
        oldMovie.setTitle("Old One");
        oldMovie.setCategory("Classic");
        oldMovie.setDescription("Old movie");
        oldMovie.setMovie_year(1999);
        oldMovie.setPrizes("none");
        oldMovie.setAgeRestriction(0);
        oldMovie.setTag("classic");

        restTemplate.postForObject(baseUrl, newMovie, Movie.class);
        restTemplate.postForObject(baseUrl, oldMovie, Movie.class);

        ResponseEntity<PageResponse<Movie>> response = restTemplate.exchange(
                baseUrl + "/findNew?page=0&size=50",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<Movie>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getContent().stream().anyMatch(m -> "new".equalsIgnoreCase(m.getTag())));
    }

    @Test
    void testMoviesSortedByAvgGradeDesc() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        var m1 = new Movie();
        m1.setTitle("High Rated");
        m1.setCategory("Drama");
        m1.setDescription("desc");
        m1.setMovie_year(2023);
        m1.setPrizes("none");
        m1.setAgeRestriction(0);

        var m2 = new Movie();
        m2.setTitle("Low Rated");
        m2.setCategory("Drama");
        m2.setDescription("desc");
        m2.setMovie_year(2023);
        m2.setPrizes("none");
        m2.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, m1, Movie.class);
        restTemplate.postForObject(baseUrl, m2, Movie.class);
        var s1 = movieRepository.save(m1);
        var s2 = movieRepository.save(m2);

        restTemplate.postForEntity(baseUrl + "/" + s1.getId() + "/grade?grade=9", request, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + s2.getId() + "/grade?grade=3", request, Double.class);

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/sortedByAvgGradeDesc?page=0&size=50",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getContent().size() >= 2);
        var first = page.getContent().get(0);
        var second = page.getContent().get(1);
        assertTrue(first.getAvgGrade() >= second.getAvgGrade());
    }

    @Test
    void testMoviesSortedByAvgGradeAsc() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        var m1 = new Movie();
        m1.setTitle("Asc High");
        m1.setCategory("Sci-Fi");
        m1.setDescription("desc");
        m1.setMovie_year(2023);
        m1.setPrizes("none");
        m1.setAgeRestriction(0);

        var m2 = new Movie();
        m2.setTitle("Asc Low");
        m2.setCategory("Sci-Fi");
        m2.setDescription("desc");
        m2.setMovie_year(2023);
        m2.setPrizes("none");
        m2.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, m1, Movie.class);
        restTemplate.postForObject(baseUrl, m2, Movie.class);
        var s1 = movieRepository.save(m1);
        var s2 = movieRepository.save(m2);

        restTemplate.postForEntity(baseUrl + "/" + s1.getId() + "/grade?grade=8", request, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + s2.getId() + "/grade?grade=2", request, Double.class);

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/sortedByAvgGradeAsc?page=0&size=50",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getContent().size() >= 2);
        var first = page.getContent().get(0);
        var second = page.getContent().get(1);
        assertTrue(first.getAvgGrade() <= second.getAvgGrade());
    }

    @Test
    void testUpdateExistingMovieGrade() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        var movie = new Movie();
        movie.setTitle("Updatable Grade");
        movie.setCategory("Thriller");
        movie.setDescription("desc");
        movie.setMovie_year(2023);
        movie.setPrizes("none");
        movie.setAgeRestriction(0);

        restTemplate.postForObject(baseUrl, movie, Movie.class);
        var saved = movieRepository.save(movie);

        var first = restTemplate.postForEntity(baseUrl + "/" + saved.getId() + "/grade?grade=3", request, Double.class);
        assertEquals(HttpStatus.CREATED, first.getStatusCode());
        assertEquals(3.0, first.getBody());

        var second = restTemplate.postForEntity(baseUrl + "/" + saved.getId() + "/grade?grade=9", request, Double.class);
        assertEquals(HttpStatus.OK, second.getStatusCode());
        assertEquals(9.0, second.getBody());

        var avg = restTemplate.getForEntity(baseUrl + "/" + saved.getId() + "/average-grade", Double.class);
        assertEquals(HttpStatus.OK, avg.getStatusCode());
        assertEquals(9.0, avg.getBody());
    }

    @Test
    void testGetMoviePropositionsForUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<Void> authRequest = new HttpEntity<>(headers);

        var m1 = new Movie();
        m1.setTitle("Prop 1");
        m1.setCategory("Action");
        m1.setDescription("desc");
        m1.setMovie_year(2024);
        m1.setPrizes("none");
        m1.setAgeRestriction(0);

        var m2 = new Movie();
        m2.setTitle("Prop 2");
        m2.setCategory("Action");
        m2.setDescription("desc");
        m2.setMovie_year(2024);
        m2.setPrizes("none");
        m2.setAgeRestriction(0);


        restTemplate.postForObject(baseUrl, m1, Movie.class);
        restTemplate.postForObject(baseUrl, m2, Movie.class);
        var s1 = movieRepository.save(m1);
        var s2 = movieRepository.save(m2);


        restTemplate.postForEntity(baseUrl + "/" + s1.getId() + "/grade?grade=7", authRequest, Double.class);
        restTemplate.postForEntity(baseUrl + "/" + s2.getId() + "/grade?grade=6", authRequest, Double.class);

        ResponseEntity<PageResponse<MovieWithAvgGradeDto>> response = restTemplate.exchange(
                baseUrl + "/propositions?page=0&size=20",
                HttpMethod.GET,
                authRequest,
                new ParameterizedTypeReference<PageResponse<MovieWithAvgGradeDto>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        var page = response.getBody();
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertTrue(page.getContent().size() > 0);
    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYearBadRequest() {
        try {
            restTemplate.getForEntity(
                    baseUrl + "/polishPremiereMonthAndYear?month=0&year=2025",
                    String.class
            );
        }catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());

        }
    }

    static class PageResponse<T> {
        private List<T> content;
        public List<T> getContent() { return content; }
        public void setContent(List<T> content) { this.content = content; }
    }
}