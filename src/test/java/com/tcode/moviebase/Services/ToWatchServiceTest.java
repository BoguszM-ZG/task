package com.tcode.moviebase.Services;



import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.ToWatch;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToWatchServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ToWatchRepository toWatchRepository;

    @InjectMocks
    private ToWatchService toWatchService;

    @Test
    void testAddToWatchMovie() {
        String userId = "user1";
        Long movieId = 1L;
        var movie = new Movie();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        var result = toWatchService.addToWatchMovie(userId, movieId);
        assertEquals(movie, result);
        verify(toWatchRepository).save(ArgumentMatchers.any(ToWatch.class));
    }

    @Test
    void testGetToWatchMovies() {
        String userId = "user1";
        var movies = List.of(new Movie());
        when(toWatchRepository.findMoviesByUserId(userId)).thenReturn(movies);
        var result = toWatchService.getToWatchMovies(userId);
        assertEquals(movies, result);
    }

    @Test
    void testExistsToWatchMovie() {
        String userId = "user1";
        Long movieId = 1L;
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);
        boolean exists = toWatchService.existsToWatchMovie(userId, movieId);
        assertTrue(exists);
    }

    @Test
    void testRemoveToWatchMovie() {
        String userId = "user1";
        Long movieId = 1L;
        toWatchService.removeToWatchMovie(userId, movieId);
        verify(toWatchRepository).removeByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void testGetToWatchMoviesByCreatedAtNewest() {
        String userId = "user1";
        var movies = List.of(new Movie());
        when(toWatchRepository.findMoviesByCreatedAt_Latest(userId)).thenReturn(movies);
        var result = toWatchService.getToWatchMoviesByCreatedAtNewest(userId);
        assertEquals(movies, result);
    }

    @Test
    void testGetToWatchMoviesByCreatedAtOldest() {
        String userId = "user1";
        var movies = List.of(new Movie());
        when(toWatchRepository.findMoviesByCreatedAt_Oldest(userId)).thenReturn(movies);
        var result = toWatchService.getToWatchMoviesByCreatedAtOldest(userId);
        assertEquals(movies, result);
    }

    @Test
    void testGetToWatchMoviesByTitleZ_A() {
        String userId = "user1";
        var movies = List.of(mock(MovieWithAvgGradeDto.class));
        when(toWatchRepository.findMoviesByTitleZ_A(userId)).thenReturn(movies);
        var result = toWatchService.getToWatchMoviesByTitleZ_A(userId);
        assertEquals(movies, result);
    }

    @Test
    void testGetToWatchMoviesByTitleA_Z() {
        String userId = "user1";
        var movies = List.of(mock(MovieWithAvgGradeDto.class));
        when(toWatchRepository.findMoviesByTitleA_Z(userId)).thenReturn(movies);
        var result = toWatchService.getToWatchMoviesByTitleA_Z(userId);
        assertEquals(movies, result);
    }

    @Test
    void testGetToWatchMoviesByCategory() {
        String userId = "user1";
        String category = "Action";
        var movies = List.of(mock(MovieWithAvgGradeDto.class));
        when(toWatchRepository.findMoviesByCategory(userId, category)).thenReturn(movies);
        var result = toWatchService.getToWatchMoviesByCategory(userId, category);
        assertEquals(movies, result);
    }
}
