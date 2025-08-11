package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.FavouriteMovie;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.FavouriteMovieRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final FavouriteMovieRepository favouriteMovieRepository;
    private final MovieRepository movieRepository;



    public Movie addFavouriteMovie(String userId, Long movieId) {

        var movie = movieRepository.findById(movieId).orElse(null);
        var favouriteMovie = new FavouriteMovie(userId, movie);
        favouriteMovieRepository.save(favouriteMovie);
        return movie;
    }

    public List<Movie> getFavouriteMovies(String userId) {
       return favouriteMovieRepository.findMoviesByUserId(userId);
    }

    @Transactional
    public void removeFavouriteMovie(String userId, Long movieId) {
        favouriteMovieRepository.deleteByUserIdAndMovieId(userId, movieId);
    }

    public boolean existsFavouriteMovie(String userId, Long movieId) {
        return favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public List<Movie> getFavouriteMoviesByCreatedAtNewest(String userId) {
        return favouriteMovieRepository.findMoviesByCreatedAt_Latest(userId);
    }

    public List<Movie> getFavouriteMoviesByCreatedAtOldest(String userId) {
        return favouriteMovieRepository.findMoviesByCreatedAt_Oldest(userId);
    }

    public List<MovieWithAvgGradeDto> getFavouriteMoviesByTitleZ_A(String userId) {
        return favouriteMovieRepository.findFavouriteMoviesByTitleZ_A(userId);
    }

    public List<MovieWithAvgGradeDto> getFavouriteMoviesByTitleA_Z(String userId) {
        return favouriteMovieRepository.findFavouriteMoviesByTitleA_Z(userId);
    }

    public List<MovieWithAvgGradeDto> getFavouriteMoviesByCategory(String userId, String category) {
        return favouriteMovieRepository.findFavouriteMoviesByCategory(userId, category);
    }

}
