package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.ToWatch;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Exceptions.ToWatchException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.ToWatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
public class ToWatchService {
    private final ToWatchRepository toWatchRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;



    public Page<MovieWithAvgGradeDto> getToWatchMovies(String userId, Pageable pageable) {
        return toWatchRepository.findMoviesByUserId(userId, pageable)
                .map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public MovieWithAvgGradeDto addToWatchMovie(String userId, Long movieId) {
        var movie = movieRepository.findById(movieId).orElseThrow( () -> new MovieNotFoundException("Movie not found"));
            if (toWatchRepository.existsByUserIdAndMovieId(userId, movieId)) {
                throw new ToWatchException("Movie is already in to-watch list");
            }
            var toWatch = new ToWatch(userId, movie);
            toWatchRepository.save(toWatch);
            return movieMapper.movieToMovieWithAvgGradeDto(movie);

    }

    @Transactional
    public void removeToWatchMovie(String userId, Long movieId) {
        if (toWatchRepository.existsByUserIdAndMovieId(userId, movieId)) {
            toWatchRepository.removeByUserIdAndMovieId(userId, movieId);
        } else {
            throw new ToWatchException("Movie not found in to-watch list");
        }

    }

    public Page<MovieWithAvgGradeDto> getToWatchMoviesByCreatedAtNewest(String userId, Pageable pageable) {
        return toWatchRepository.findMoviesByCreatedAt_Latest(userId, pageable)
                .map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getToWatchMoviesByCreatedAtOldest(String userId, Pageable pageable) {
        return toWatchRepository.findMoviesByCreatedAt_Oldest(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getToWatchMoviesByTitleZ_A(String userId, Pageable pageable) {
        return toWatchRepository.findMoviesByTitleZ_A(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getToWatchMoviesByTitleA_Z(String userId, Pageable pageable) {
        return toWatchRepository.findMoviesByTitleA_Z(userId, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getToWatchMoviesByCategory(String userId, String category, Pageable pageable) {
        return toWatchRepository.findMoviesByCategory(userId, category, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }
}
