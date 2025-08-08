package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieJuniorServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieJuniorService movieJuniorService;

    @Test
    void testGetMoviesForJuniors() {
        Movie m1 = new Movie(); m1.setAgeRestriction(0);
        Movie m2 = new Movie(); m2.setAgeRestriction(17);
        Movie m3 = new Movie(); m3.setAgeRestriction(18);
        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2, m3));

        List<Movie> result = movieJuniorService.getMoviesForJuniors();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getAgeRestriction() <= 17));
    }

    @Test
    void testGetMovieByIdJuniors() {
        Long id = 1L;
        Movie movie = new Movie(); movie.setAgeRestriction(15);
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));

        Optional<?> result = movieJuniorService.getMovieByIdJuniors(id);

        assertTrue(result.isPresent());
        assertEquals(15, ((Movie) result.get()).getAgeRestriction());
    }

    @Test
    void testGetMovieByIdJuniorsReturnsErrorIfNotForJunior() {
        Long id = 2L;
        Movie movie = new Movie(); movie.setAgeRestriction(18);
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));

        Optional<?> result = movieJuniorService.getMovieByIdJuniors(id);

        assertTrue(result.isPresent());
        assertEquals("This movie is not suitable for juniors", result.get());
    }

    @Test
    void testGetMovieByIdJuniorsReturnsErrorIfMovieNotFound() {
        Long id = 3L;
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        Optional<?> result = movieJuniorService.getMovieByIdJuniors(id);

        assertTrue(result.isPresent());
        assertEquals("Movie not found", result.get());
    }

    @Test
    void testFindByCategoryForJuniors() {
        String category = "Action";
        Movie m1 = new Movie(); m1.setAgeRestriction(10); m1.setCategory(category);
        Movie m2 = new Movie(); m2.setAgeRestriction(19); m2.setCategory(category);
        when(movieRepository.findMoviesByCategory(category)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieJuniorService.findByCategoryForJuniors(category);

        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().getAgeRestriction());
    }

    @Test
    void testSearchMovieByTitleForJuniors() {
        String title = "junior";
        Movie m1 = new Movie(); m1.setAgeRestriction(0);
        Movie m2 = new Movie(); m2.setAgeRestriction(18);
        when(movieRepository.findByTitleIgnoreCaseContaining(title)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieJuniorService.searchForJuniors(title);

        assertEquals(1, result.size());
        assertEquals(0, result.getFirst().getAgeRestriction());
    }

    @Test
    void testFindNewMoviesForJuniors() {
        String tag = "new";
        Movie m1 = new Movie(); m1.setAgeRestriction(17); m1.setTag(tag);
        Movie m2 = new Movie(); m2.setAgeRestriction(20); m2.setTag(tag);
        when(movieRepository.findByTagContaining(tag)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieJuniorService.findMoviesForJuniorsByTag(tag);

        assertEquals(1, result.size());
        assertEquals(17, result.getFirst().getAgeRestriction());
    }

    @Test
    void testGetMoviesByPremiereYearForJuniors() {
        int year = 2022;
        Movie m1 = new Movie(); m1.setAgeRestriction(0); m1.setMovie_year(year);
        Movie m2 = new Movie(); m2.setAgeRestriction(18); m2.setMovie_year(year);
        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieJuniorService.getMoviesByPremiereYearForJuniors(year);

        assertEquals(1, result.size());
        assertEquals(0, result.getFirst().getAgeRestriction());
    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYearForJuniors() {
        int month = 5;
        int year = 2023;
        Movie m1 = new Movie(); m1.setAgeRestriction(17);
        m1.setPolish_premiere(LocalDate.of(year, month, 1));
        Movie m2 = new Movie(); m2.setAgeRestriction(18);
        m2.setPolish_premiere(LocalDate.of(year, month, 1));
        when(movieRepository.findMovieByPolishPremiereMonthAndYear(month, year)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieJuniorService.getMoviesByPolishPremiereMonthAndYearForJuniors(month, year);

        assertEquals(1, result.size());
        assertEquals(17, result.getFirst().getAgeRestriction());
    }

    @Test
    void testGetMoviesByWorldPremiereMonthAndYearForJuniors() {
        int month = 6;
        int year = 2023;
        Movie m1 = new Movie(); m1.setAgeRestriction(10);
        m1.setWorld_premiere(LocalDate.of(year, month, 1));
        Movie m2 = new Movie(); m2.setAgeRestriction(19);
        m2.setWorld_premiere(LocalDate.of(year, month, 1));
        when(movieRepository.findMovieByWorldPremiereMonthAndYear(month, year)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieJuniorService.getMoviesByWorldPremiereMonthAndYearForJuniors(month, year);

        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().getAgeRestriction());
    }

    @Test
    void testGetMoviesWithAvgGradeDescForJuniors() {
        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 17, 4.5);
        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 18, 3.0);
        when(movieRepository.findAllMoviesWithAvgGradeDesc()).thenReturn(Arrays.asList(m1, m2));

        List<MovieWithAvgGradeDto> result = movieJuniorService.getMoviesWithAvgGradeDescForJuniors();

        assertEquals(1, result.size());
        assertEquals(17, result.getFirst().getAgeRestriction());
    }

    @Test
    void testGetMoviesWithAvgGradeAscForJuniors() {
        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "asc", "prize", LocalDate.now(), LocalDate.now(), "tag", 0, 2.5);
        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "asc", "prize", LocalDate.now(), LocalDate.now(), "tag", 18, 3.5);
        when(movieRepository.findAllMoviesWithAvgGradeAsc()).thenReturn(Arrays.asList(m1, m2));

        List<MovieWithAvgGradeDto> result = movieJuniorService.getMoviesWithAvgGradeAscForJuniors();

        assertEquals(1, result.size());
        assertEquals(0, result.getFirst().getAgeRestriction());
    }

    @Test
    void testGetTopTenMoviesWithAvgGradeForJuniors() {
        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 10, 5.0);
        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 20, 4.0);
        when(movieRepository.findTop10MoviesByAvgGrade()).thenReturn(Arrays.asList(m1, m2));

        List<MovieWithAvgGradeDto> result = movieJuniorService.getTopTenMoviesWithAvgGradeForJuniors();

        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().getAgeRestriction());
    }
}