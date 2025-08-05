package com.tcode.moviebase.Services;

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
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;



    @InjectMocks
    private MovieService movieService;



    @Test
    void getAllMoviesReturnsList() {
        Movie m1 = new Movie();
        Movie m2 = new Movie();
        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.getAllMovies();

        assertEquals(2, result.size());
    }

    @Test
    void addMovieSavesMovie() {
        Movie movie = new Movie();
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.addMovie(movie);

        assertNotNull(result);
        verify(movieRepository).save(movie);
    }

    @Test
    void deleteMovieDeletesById() {
        Long id = 1L;

        movieService.deleteMovie(id);
        verify(movieRepository).deleteMovieById(id);
    }

    @Test
    void search_returnsMatchingMovies() {
        String search = "test";
        Movie m1 = new Movie();
        m1.setTitle("Test Movie");
        Movie m2 = new Movie();
        m2.setTitle("Another Test Movie");
        when(movieRepository.findByTitleIgnoreCaseContaining(search)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.search(search);

        assertEquals(2, result.size());
        verify(movieRepository).findByTitleIgnoreCaseContaining(search);
    }

    @Test
    void findByCategory_returnsMoviesByCategory() {
        String category = "Action";
        Movie m1 = new Movie();
        m1.setCategory(category);
        Movie m2 = new Movie();
        m2.setCategory(category);
        when(movieRepository.findMoviesByCategory(category)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.findByCategory(category);

        assertEquals(2, result.size());
        verify(movieRepository).findMoviesByCategory(category);
    }



    @Test
    void getAllMoviesReturnsEmptyListWhenNoMovies() {
        when(movieRepository.findAll()).thenReturn(List.of());

        List<Movie> result = movieService.getAllMovies();

        assertTrue(result.isEmpty());
        verify(movieRepository).findAll();
    }

    @Test
    void searchReturnsEmptyListWhenNoMatches() {
        String search = "test";
        when(movieRepository.findByTitleIgnoreCaseContaining(search)).thenReturn(List.of());

        List<Movie> result = movieService.search(search);

        assertTrue(result.isEmpty());
        verify(movieRepository).findByTitleIgnoreCaseContaining(search);
    }

    @Test
    void findByCategoryReturnsEmptyListWhenNoMatches() {
        String category = "test";
        when(movieRepository.findMoviesByCategory(category)).thenReturn(List.of());

        List<Movie> result = movieService.findByCategory(category);

        assertTrue(result.isEmpty());
        verify(movieRepository).findMoviesByCategory(category);
    }



    @Test
    void updateMovieTest() {
        Long id = 1L;
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle("test");
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);


        Long id2 = 2L;
        Movie movie2 = new Movie();
        movie2.setId(id2);
        movie2.setTitle("test2");
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.updateMovie(id, movie2);

        assertNotNull(result);
        assertEquals("test2", result.getTitle());
    }





    @Test
    void getMovieByIdReturnsMovieWhenExists() {
        Long id = 12L;
        Movie movie = new Movie();
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(id);

        assertNotNull(result);
        verify(movieRepository).findById(id);
    }


    @Test
    void getMoviesByTagReturnsMoviesWithTag() {
        String tag = "new";
        Movie m1 = new Movie();
        m1.setTag(tag);
        Movie m2 = new Movie();
        m2.setTag(tag);
        when(movieRepository.findByTagContaining(tag)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.getMoviesByTag(tag);

        assertEquals(2, result.size());
        verify(movieRepository).findByTagContaining(tag);
    }

    @Test
    void testGetMoviesByPremiereYear() {
        int premiereYear = 2023;
        Movie m1 = new Movie();
        m1.setMovie_year(premiereYear);
        Movie m2 = new Movie();
        m2.setMovie_year(premiereYear);
        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.getMoviesByPremiereYear(premiereYear);

        assertEquals(2, result.size());
        verify(movieRepository).findAll();
    }

    @Test
    void testGetMoviesByPremiereYearReturnsEmptyListWhenNoMatches() {
        int premiereYear = 2023;
        when(movieRepository.findAll()).thenReturn(List.of());

        List<Movie> result = movieService.getMoviesByPremiereYear(premiereYear);

        assertTrue(result.isEmpty());
        verify(movieRepository).findAll();
    }

    @Test
    void testGetMoviesByWorldPremiereMonthAndYear() {
        int month = 5;
        int year = 2023;
        Movie m1 = new Movie();
        m1.setWorld_premiere(LocalDate.of(year, month, 15));
        Movie m2 = new Movie();
        m2.setWorld_premiere(LocalDate.of(year, month, 20));
        when(movieRepository.findMovieByWorldPremiereMonthAndYear(month, year)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.getMoviesByWorldPremiereMonthAndYear(month, year);

        assertEquals(2, result.size());
        verify(movieRepository).findMovieByWorldPremiereMonthAndYear(month, year);
    }

    @Test
    void testGetMoviesByPolishPremiereMonthAndYear() {
        int month = 6;
        int year = 2023;
        Movie m1 = new Movie();
        m1.setPolish_premiere(LocalDate.of(year, month, 10));
        Movie m2 = new Movie();
        m2.setPolish_premiere(LocalDate.of(year, month, 25));
        when(movieRepository.findMovieByPolishPremiereMonthAndYear(month, year)).thenReturn(Arrays.asList(m1, m2));

        List<Movie> result = movieService.getMoviesByPolishPremiereMonthAndYear(month, year);

        assertEquals(2, result.size());
        verify(movieRepository).findMovieByPolishPremiereMonthAndYear(month, year);
    }











}
