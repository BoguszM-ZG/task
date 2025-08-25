package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.WatchedMovie;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.WatchedMovieException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieNotificationRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchedServiceTest {

    @Mock
    private WatchedMoviesRepository watchedMoviesRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ToWatchRepository toWatchRepository;
    @Mock
    private MovieNotificationRepository movieNotificationRepository;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private WatchedService watchedService;

    private final String userId = "user-1";
    private final Long movieId = 10L;

    @Test
    void addWatchedMovie_success_withCleanup() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        when(movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var result = watchedService.addWatchedMovie(userId, movieId);

        assertEquals(dto, result);
        ArgumentCaptor<WatchedMovie> captor = ArgumentCaptor.forClass(WatchedMovie.class);
        verify(watchedMoviesRepository).save(captor.capture());
        assertEquals(userId, captor.getValue().getUserId());
        assertEquals(movie, captor.getValue().getMovie());
        verify(movieNotificationRepository).deleteByUserIdAndMovieId(userId, movieId);
        verify(toWatchRepository).removeByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void addWatchedMovie_success_noCleanup() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        when(movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        watchedService.addWatchedMovie(userId, movieId);

        verify(movieNotificationRepository, never()).deleteByUserIdAndMovieId(anyString(), anyLong());
        verify(toWatchRepository, never()).removeByUserIdAndMovieId(anyString(), anyLong());
    }

    @Test
    void addWatchedMovie_alreadyExists() {
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(mock(Movie.class)));
        when(watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        assertThrows(WatchedMovieException.class,
                () -> watchedService.addWatchedMovie(userId, movieId));

        verify(watchedMoviesRepository, never()).save(any());
    }

    @Test
    void addWatchedMovie_movieNotFound() {
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class,
                () -> watchedService.addWatchedMovie(userId, movieId));
        verify(watchedMoviesRepository, never()).save(any());
    }

    @Test
    void getWatchedMoviesByUserId() {
        Pageable pageable = PageRequest.of(0, 3);
        var movie1 = mock(Movie.class);
        var movie2 = mock(Movie.class);
        var dto1 = mock(MovieWithAvgGradeDto.class);
        var dto2 = mock(MovieWithAvgGradeDto.class);

        when(watchedMoviesRepository.findAllMoviesByUserId(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie1, movie2), pageable, 2));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie1)).thenReturn(dto1);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie2)).thenReturn(dto2);

        Page<MovieWithAvgGradeDto> page = watchedService.getWatchedMoviesByUserId(userId, pageable);

        assertEquals(2, page.getTotalElements());
        assertEquals(List.of(dto1, dto2), page.getContent());
    }

    @Test
    void removeWatchedMovie_success() {
        when(movieRepository.existsById(movieId)).thenReturn(true);
        when(watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        watchedService.removeWatchedMovie(userId, movieId);

        verify(watchedMoviesRepository).removeByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void removeWatchedMovie_movieNotFound() {
        when(movieRepository.existsById(movieId)).thenReturn(false);

        assertThrows(MovieNotFoundException.class,
                () -> watchedService.removeWatchedMovie(userId, movieId));

        verify(watchedMoviesRepository, never()).removeByUserIdAndMovieId(anyString(), anyLong());
    }

    @Test
    void removeWatchedMovie_notInList() {
        when(movieRepository.existsById(movieId)).thenReturn(true);
        when(watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);

        assertThrows(WatchedMovieException.class,
                () -> watchedService.removeWatchedMovie(userId, movieId));

        verify(watchedMoviesRepository, never()).removeByUserIdAndMovieId(anyString(), anyLong());
    }

    @Test
    void countWatchedMovies() {
        when(watchedMoviesRepository.findMoviesByUserId(userId))
                .thenReturn(List.of(new Movie(), new Movie(), new Movie()));

        assertEquals(3, watchedService.countWatchedMovies(userId));
        verify(watchedMoviesRepository).findMoviesByUserId(userId);
    }

    @Test
    void countWatchedMoviesInMonth() {
        when(watchedMoviesRepository.findWatchedMoviesByCreatedAt(userId, 2024, 8)).thenReturn(7);

        assertEquals(7, watchedService.countWatchedMoviesInMonth(userId, 2024, 8));
        verify(watchedMoviesRepository).findWatchedMoviesByCreatedAt(userId, 2024, 8);
    }
}