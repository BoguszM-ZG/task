package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.FavouriteMovie;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.FavouriteMovieRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private FavouriteService favouriteService;



    @Test
    void testAddFavouriteMovie() {
        String userId = "user1";
        Long movieId = 1L;
        var movie = new Movie();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        var result = favouriteService.addFavouriteMovie(userId, movieId);

        assertEquals(movie, result);
        verify(favouriteMovieRepository).save(ArgumentMatchers.any(FavouriteMovie.class));
    }

    @Test
    void testGetFavouriteMovies() {
        String userId = "user1";
        var movies = List.of(new Movie());
        when(favouriteMovieRepository.findMoviesByUserId(userId)).thenReturn(movies);

        var result = favouriteService.getFavouriteMovies(userId);

        assertEquals(movies, result);
    }

    @Test
    void testRemoveFavouriteMovie() {
        String userId = "user1";
        Long movieId = 1L;

        favouriteService.removeFavouriteMovie(userId, movieId);

        verify(favouriteMovieRepository).deleteByUserIdAndMovieId(userId, movieId);
    }

    @Test
    void testExistsFavouriteMovie() {
        String userId = "user1";
        Long movieId = 1L;
        when(favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)).thenReturn(true);

        assertTrue(favouriteService.existsFavouriteMovie(userId, movieId));
    }

    @Test
    void testGetFavouriteMoviesByCreatedAtNewest() {
        String userId = "user1";
        var movies = List.of(new Movie());
        when(favouriteMovieRepository.findMoviesByCreatedAt_Latest(userId)).thenReturn(movies);

        var result = favouriteService.getFavouriteMoviesByCreatedAtNewest(userId);

        assertEquals(movies, result);
    }

    @Test
    void testGetFavouriteMoviesByCreatedAtOldest() {
        String userId = "user1";
        var movies = List.of(new Movie());
        when(favouriteMovieRepository.findMoviesByCreatedAt_Oldest(userId)).thenReturn(movies);

        var result = favouriteService.getFavouriteMoviesByCreatedAtOldest(userId);

        assertEquals(movies, result);
    }

    @Test
    void testGetFavouriteMoviesByTitleZ_A() {
        String userId = "user1";
        var dtos = List.of(mock(MovieWithAvgGradeDto.class));
        when(favouriteMovieRepository.findFavouriteMoviesByTitleZ_A(userId)).thenReturn(dtos);

        var result = favouriteService.getFavouriteMoviesByTitleZ_A(userId);

        assertEquals(dtos, result);
    }

    @Test
    void testGetFavouriteMoviesByTitleA_Z() {
        String userId = "user1";
        var dtos = List.of(mock(MovieWithAvgGradeDto.class));
        when(favouriteMovieRepository.findFavouriteMoviesByTitleA_Z(userId)).thenReturn(dtos);

        var result = favouriteService.getFavouriteMoviesByTitleA_Z(userId);

        assertEquals(dtos, result);
    }

    @Test
    void testGetFavouriteMoviesByCategory() {
        String userId = "user1";
        String category = "Action";
        var dtos = List.of(mock(MovieWithAvgGradeDto.class));
        when(favouriteMovieRepository.findFavouriteMoviesByCategory(userId, category)).thenReturn(dtos);

        var result = favouriteService.getFavouriteMoviesByCategory(userId, category);

        assertEquals(dtos, result);
    }
}