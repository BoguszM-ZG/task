package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.FavouriteMovie;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.MovieAlreadyInFavouritesException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.MovieNotFoundInFavouritesException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.FavouriteMovieRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.DisplayName;
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
class FavouriteServiceTest {

    @Mock
    private FavouriteMovieRepository favouriteMovieRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private FavouriteService favouriteService;

    private final String userId = "user-123";
    private final Long movieId = 10L;
    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    void addFavouriteMovie_success() {
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var result = favouriteService.addFavouriteMovie(userId, movieId);

        assertEquals(dto, result);
        verify(favouriteMovieRepository).save(any(FavouriteMovie.class));
    }

    @Test
    void addFavouriteMovie_movieNotFound() {
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class,
                () -> favouriteService.addFavouriteMovie(userId, movieId));
        verify(favouriteMovieRepository, never()).save(any());
    }

    @Test
    void addFavouriteMovie_alreadyInFavourites() {
        Movie movie = mock(Movie.class);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        assertThrows(MovieAlreadyInFavouritesException.class,
                () -> favouriteService.addFavouriteMovie(userId, movieId));
        verify(favouriteMovieRepository, never()).save(any());
    }

    @Test
    void getFavouriteMovies() {
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);
        when(favouriteMovieRepository.findMoviesByUserId(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        Page<MovieWithAvgGradeDto> page = favouriteService.getFavouriteMovies(userId, pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void removeFavouriteMovie_success() {
        when(favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        favouriteService.removeFavouriteMovie(userId, movieId);

        verify(favouriteMovieRepository).deleteByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void removeFavouriteMovie_notFound() {
        when(favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(false);

        assertThrows(MovieNotFoundInFavouritesException.class,
                () -> favouriteService.removeFavouriteMovie(userId, movieId));
        verify(favouriteMovieRepository, never()).deleteByUserIdAndMovieId(any(), any());
    }

    @Test
    void existsFavouriteMovie_true() {
        when(favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);
        assertTrue(favouriteService.existsFavouriteMovie(userId, movieId));
    }

    @Test
    void getFavouriteMoviesByCreatedAtNewest() {
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);
        when(favouriteMovieRepository.findMoviesByCreatedAt_Latest(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = favouriteService.getFavouriteMoviesByCreatedAtNewest(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getFavouriteMoviesByCreatedAtOldest() {
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);
        when(favouriteMovieRepository.findMoviesByCreatedAt_Oldest(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = favouriteService.getFavouriteMoviesByCreatedAtOldest(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getFavouriteMoviesByTitleZ_A() {
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);
        when(favouriteMovieRepository.findFavouriteMovieByTitleZ_A(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = favouriteService.getFavouriteMoviesByTitleZ_A(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getFavouriteMoviesByTitleA_Z() {
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);
        when(favouriteMovieRepository.findFavouriteMovieByTitleA_Z(userId, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = favouriteService.getFavouriteMoviesByTitleA_Z(userId, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }

    @Test
    void getFavouriteMoviesByCategory() {
        String category = "Action";
        Movie movie = mock(Movie.class);
        MovieWithAvgGradeDto dto = mock(MovieWithAvgGradeDto.class);
        when(favouriteMovieRepository.findFavouriteMoviesByCategory(userId, category, pageable))
                .thenReturn(new PageImpl<>(List.of(movie), pageable, 1));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        var page = favouriteService.getFavouriteMoviesByCategory(userId, category, pageable);
        assertEquals(dto, page.getContent().getFirst());
    }
}