package com.tcode.moviebase.Services;


import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.WatchedMovie;
import com.tcode.moviebase.Exceptions.InvalidMonthException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.WatchedMovieException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieNotificationRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WatchedService {
    private final WatchedMoviesRepository watchedMoviesRepository;
    private final MovieRepository movieRepository;
    private final ToWatchRepository toWatchRepository;
    private final MovieNotificationRepository movieNotificationRepository;
    private final MovieMapper movieMapper;


    @Transactional
    public MovieWithAvgGradeDto addWatchedMovie(String userId, Long movieId) {
        var movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        if (watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new WatchedMovieException("Movie is already in watched list");
        }
            if (movieNotificationRepository.existsByUserIdAndMovieId(userId, movieId)) {
                movieNotificationRepository.deleteByUserIdAndMovieId(userId, movieId);
            }
            if (toWatchRepository.existsByUserIdAndMovieId(userId, movieId)) {
                toWatchRepository.removeByUserIdAndMovieId(userId, movieId);
            }
            var watchedMovie = new WatchedMovie(userId, movie);
            watchedMoviesRepository.save(watchedMovie);

        return  movieMapper.movieToMovieWithAvgGradeDto(movie);
    }


    public Page<MovieWithAvgGradeDto> getWatchedMoviesByUserId(String userId, Pageable pageable) {
        var moviesPage = watchedMoviesRepository.findAllMoviesByUserId(userId, pageable);
        return moviesPage.map(movieMapper::movieToMovieWithAvgGradeDto);
    }



    @Transactional
    public void removeWatchedMovie(String userId, Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Movie not found");
        }
        if (!watchedMoviesRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new WatchedMovieException("Movie is not in watched list");
        }

        watchedMoviesRepository.removeByUserIdAndMovieId(userId, movieId);
    }

    public Integer countWatchedMovies(String userId) {
        return watchedMoviesRepository.findMoviesByUserId(userId).size();
    }

    public Integer countWatchedMoviesInMonth(String userId, int year, int month) {
        if (month < 1 || month > 12) {
            throw new InvalidMonthException("Invalid month");
        }
        return watchedMoviesRepository.findWatchedMoviesByCreatedAt(userId, year, month);

    }
}
