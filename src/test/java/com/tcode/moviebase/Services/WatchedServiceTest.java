package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.WatchedMovie;
import com.tcode.moviebase.Repositories.MovieNotificationRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WatchedServiceTest {

    @Mock
    private WatchedMoviesRepository watchedMoviesRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ToWatchRepository toWatchRepository;

    @Mock
    private MovieNotificationRepository movieNotificationRepository;

    @InjectMocks
    private WatchedService watchedService;

    @Test
    void testExistsWatchedMovie() {
        String userId = "user1";
        Long movieId = 1L;

        watchedService.existsWatchedMovie(userId, movieId);
        verify(watchedMoviesRepository).existsByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void testCountWatchedMovies() {
        String userId = "user1";
        watchedService.addWatchedMovie(userId, 1L);

        when(watchedMoviesRepository.findMoviesByUserId(userId)).thenReturn(List.of(new Movie()));
        int count = watchedService.countWatchedMovies(userId);

        verify(watchedMoviesRepository).findMoviesByUserId(userId);
        assertEquals(1, count);
    }

    @Test
    void testCountWatchedMoviesInMonth() {
        String userId = "user1";
        int year = 2023;
        int month = 10;

        when(watchedMoviesRepository.findWatchedMoviesByCreatedAt(userId, year, month)).thenReturn(5);


        assertEquals(5, watchedService.countWatchedMoviesInMonth(userId, year, month));
        verify(watchedMoviesRepository).findWatchedMoviesByCreatedAt(userId, year, month);
    }

    @Test
    void testAddWatchedMovie() {
        String userId = "user1";
        Long movieId = 1L;
        Movie movie = new Movie();

        when(movieRepository.findById(movieId)).thenReturn(java.util.Optional.of(movie));
        when(movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);
        when(watchedMoviesRepository.save(org.mockito.ArgumentMatchers.any(WatchedMovie.class)))
                .thenReturn(new WatchedMovie(userId, movie));

        watchedService.addWatchedMovie(userId, movieId);

        verify(movieRepository).findById(movieId);
        verify(watchedMoviesRepository).save(org.mockito.ArgumentMatchers.any(WatchedMovie.class));
        verify(movieNotificationRepository).deleteByUserIdAndMovieId(userId, movieId);
        verify(toWatchRepository).removeByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void testGetWatchedMovies() {
        String userId = "user1";
        List<Movie> movies = List.of(new Movie(), new Movie());

        when(watchedMoviesRepository.findMoviesByUserId(userId)).thenReturn(movies);

        List<Movie> result = watchedService.getWatchedMovies(userId);

        verify(watchedMoviesRepository).findMoviesByUserId(userId);
        assertEquals(movies, result);
    }

    @Test
    void testRemoveWatchedMovie() {
        String userId = "user1";
        Long movieId = 1L;

        watchedService.removeWatchedMovie(userId, movieId);
        verify(watchedMoviesRepository).removeByUserIdAndMovieId(userId, movieId);
    }


}
