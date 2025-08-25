package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.ToWatch;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.ToWatchException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
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
class ToWatchServiceTest {

    @Mock
    private ToWatchRepository toWatchRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private ToWatchService toWatchService;

    private final String userId = "user-1";
    private final Long movieId = 10L;
    private final Pageable pageable = PageRequest.of(0, 5);

    @Test
    void addToWatchMovie_success() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var result = toWatchService.addToWatchMovie(userId, movieId);

        assertEquals(dto, result);
        verify(toWatchRepository).save(any(ToWatch.class));
    }

    @Test
    void addToWatchMovie_movieNotFound() {
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class,
                () -> toWatchService.addToWatchMovie(userId, movieId));
        verify(toWatchRepository, never()).save(any());
    }

    @Test
    void addToWatchMovie_duplicate() {
        var movie = mock(Movie.class);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        assertThrows(ToWatchException.class,
                () -> toWatchService.addToWatchMovie(userId, movieId));
        verify(toWatchRepository, never()).save(any());
    }

    @Test
    void getToWatchMovies() {
        var movie1 = mock(Movie.class);
        var movie2 = mock(Movie.class);
        var dto1 = mock(MovieWithAvgGradeDto.class);
        var dto2 = mock(MovieWithAvgGradeDto.class);

        when(toWatchRepository.findMoviesByUserId(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie1, movie2), pageable, 2));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie1)).thenReturn(dto1);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie2)).thenReturn(dto2);

        Page<MovieWithAvgGradeDto> page = toWatchService.getToWatchMovies(userId, pageable);

        assertEquals(2, page.getTotalElements());
        assertEquals(List.of(dto1, dto2), page.getContent());
    }

    @Test
    void removeToWatchMovie_success() {
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        toWatchService.removeToWatchMovie(userId, movieId);

        verify(toWatchRepository).removeByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void removeToWatchMovie_notFound() {
        when(toWatchRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        assertThrows(ToWatchException.class,
                () -> toWatchService.removeToWatchMovie(userId, movieId));
        verify(toWatchRepository, never()).removeByUserIdAndMovieId(anyString(), anyLong());
    }

    @Test
    void getToWatchMoviesByCreatedAtNewest() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);
        when(toWatchRepository.findMoviesByCreatedAt_Latest(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = toWatchService.getToWatchMoviesByCreatedAtNewest(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getToWatchMoviesByCreatedAtOldest() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);
        when(toWatchRepository.findMoviesByCreatedAt_Oldest(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = toWatchService.getToWatchMoviesByCreatedAtOldest(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getToWatchMoviesByTitleZ_A() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);
        when(toWatchRepository.findMoviesByTitleZ_A(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = toWatchService.getToWatchMoviesByTitleZ_A(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getToWatchMoviesByTitleA_Z() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);
        when(toWatchRepository.findMoviesByTitleA_Z(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = toWatchService.getToWatchMoviesByTitleA_Z(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getToWatchMoviesByCategory() {
        var movie = mock(Movie.class);
        var dto = mock(MovieWithAvgGradeDto.class);
        when(toWatchRepository.findMoviesByCategory(userId, "Action", pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = toWatchService.getToWatchMoviesByCategory(userId, "Action", pageable);
        assertEquals(dto, page.getContent().getFirst());
    }
}