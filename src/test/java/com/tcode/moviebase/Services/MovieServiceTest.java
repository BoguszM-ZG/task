package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Movie;

import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Repositories.MovieGradeRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieGradeRepository movieGradeRepository;

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
    void addGradeTest() {
        Long movieId = 1L;
        int grade = 5;
        Movie movie = new Movie();
        MovieGrade movieGrade = new MovieGrade();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieGradeRepository.save(any(MovieGrade.class))).thenReturn(movieGrade);

        MovieGrade result = movieService.addGrade(movieId, grade);

        assertEquals(movieGrade, result);
        verify(movieRepository).findById(movieId);
        verify(movieGradeRepository).save(any(MovieGrade.class));
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
    void addGradeReturnsNullWhenMovieNotFound() {
        Long movieId = 1L;
        int grade = 5;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        MovieGrade result = movieService.addGrade(movieId, grade);

        assertNull(result);
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





}
