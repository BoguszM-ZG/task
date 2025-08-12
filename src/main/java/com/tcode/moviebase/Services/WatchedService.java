package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.WatchedMovie;
import com.tcode.moviebase.Repositories.MovieNotificationRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchedService {
    private final WatchedMoviesRepository watchedMoviesRepository;
    private final MovieRepository movieRepository;
    private final ToWatchRepository toWatchRepository;
    private final MovieNotificationRepository movieNotificationRepository;

    public boolean existsWatchedMovie(String userId, Long movieId) {
        return watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    @Transactional
    public Movie addWatchedMovie(String userId, Long movieId) {
        var movie = movieRepository.findById(movieId).orElse(null);

        if (movie != null) {
            if (movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)) {
                movieNotificationRepository.deleteByUserIdAndMovieId(userId, movieId);
            }
            if (toWatchRepository.existsByUserIdAndMovieId(userId, movieId)) {
                toWatchRepository.removeByUserIdAndMovieId(userId, movieId);
            }
            var watchedMovie = new WatchedMovie(userId, movie);
            watchedMoviesRepository.save(watchedMovie);
        }
        return  movie;
    }

    public List<Movie> getWatchedMovies(String userId) {
        return watchedMoviesRepository.findMoviesByUserId(userId);
    }

    @Transactional
    public void removeWatchedMovie(String userId, Long movieId) {
        watchedMoviesRepository.removeByUserIdAndMovieId(userId, movieId);
    }
}
