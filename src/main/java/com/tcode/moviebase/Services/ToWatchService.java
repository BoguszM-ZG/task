package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.ToWatch;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToWatchService {
    private final ToWatchRepository toWatchRepository;
    private final MovieRepository movieRepository;

    public boolean existsToWatchMovie(String userId, Long movieId) {
        return toWatchRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public List<Movie> getToWatchMovies(String userId) {
        return toWatchRepository.findMoviesByUserId(userId);
    }

    public Movie addToWatchMovie(String userId, Long movieId) {
        var movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            var toWatch = new ToWatch(userId, movie);
            toWatchRepository.save(toWatch);
        }
        return movie;
    }

    @Transactional
    public void removeToWatchMovie(String userId, Long movieId) {
        toWatchRepository.removeByUserIdAndMovieId(userId, movieId);
    }

    public List<Movie> getToWatchMoviesByCreatedAtNewest(String userId) {
        return toWatchRepository.findMoviesByCreatedAt_Latest(userId);
    }

    public List<Movie> getToWatchMoviesByCreatedAtOldest(String userId) {
        return toWatchRepository.findMoviesByCreatedAt_Oldest(userId);
    }

    public List<MovieWithAvgGradeDto> getToWatchMoviesByTitleZ_A(String userId) {
        return toWatchRepository.findMoviesByTitleZ_A(userId);
    }

    public List<MovieWithAvgGradeDto> getToWatchMoviesByTitleA_Z(String userId) {
        return toWatchRepository.findMoviesByTitleA_Z(userId);
    }

    public List<MovieWithAvgGradeDto> getToWatchMoviesByCategory(String userId, String category) {
        return toWatchRepository.findMoviesByCategory(userId, category);
    }
}
