package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.FavouriteMovie;
import com.tcode.moviebase.Exceptions.MovieAlreadyInFavouritesException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.MovieNotFoundInFavouritesException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.FavouriteMovieRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
public class FavouriteService {
    private final FavouriteMovieRepository favouriteMovieRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;



    public MovieWithAvgGradeDto addFavouriteMovie(String userId, Long movieId) {

        var movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        if (favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new MovieAlreadyInFavouritesException("Movie already in favourites");
        }
        var favouriteMovie = new FavouriteMovie(userId, movie);
        favouriteMovieRepository.save(favouriteMovie);
        return movieMapper.movieToMovieWithAvgGradeDto(movie);
    }

    public Page<MovieWithAvgGradeDto> getFavouriteMovies(String userId, Pageable pageable) {
       return favouriteMovieRepository.findMoviesByUserId(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    @Transactional
    public void removeFavouriteMovie(String userId, Long movieId) {
        if (favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)) {
            favouriteMovieRepository.deleteByUserIdAndMovieId(userId, movieId);
        }
        else  {
            throw new MovieNotFoundInFavouritesException("Movie not found in favourites");
        }
    }

    public boolean existsFavouriteMovie(String userId, Long movieId) {
        return favouriteMovieRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public Page<MovieWithAvgGradeDto> getFavouriteMoviesByCreatedAtNewest(String userId, Pageable pageable) {
        return favouriteMovieRepository.findMoviesByCreatedAt_Latest(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getFavouriteMoviesByCreatedAtOldest(String userId, Pageable pageable) {
        return favouriteMovieRepository.findMoviesByCreatedAt_Oldest(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getFavouriteMoviesByTitleZ_A(String userId, Pageable pageable) {
        return favouriteMovieRepository.findFavouriteMovieByTitleZ_A(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getFavouriteMoviesByTitleA_Z(String userId, Pageable pageable) {
        return favouriteMovieRepository.findFavouriteMovieByTitleA_Z(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getFavouriteMoviesByCategory(String userId, String category, Pageable pageable) {
        return favouriteMovieRepository.findFavouriteMoviesByCategory(userId, category, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

}
