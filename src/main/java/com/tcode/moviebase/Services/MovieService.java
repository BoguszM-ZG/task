package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.InvalidMovieDataException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final WatchedMoviesRepository watchedMoviesRepository;
    private final MovieMapper movieMapper;


    public MovieWithAvgGradeDto addMovie(Movie movie) {
        if (movie.getTitle() == null || movie.getMovie_year() == null) {
            throw new InvalidMovieDataException("Title and movie year are required");
        }
        movieRepository.save(movie);
        return movieMapper.movieToMovieWithAvgGradeDto(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new MovieNotFoundException("Movie not found");
        }
    }


    public MovieWithAvgGradeDto updateMovie(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found"));


        existingMovie.setCategory(movie.getCategory());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setPrizes(movie.getPrizes());
        existingMovie.setTag(movie.getTag());
        existingMovie.setWorld_premiere(movie.getWorld_premiere());
        existingMovie.setPolish_premiere(movie.getPolish_premiere());

        movieRepository.save(existingMovie);


        return movieMapper.movieToMovieWithAvgGradeDto(existingMovie);
    }


    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie not found"));
    }


    public Page<MovieWithAvgGradeDto> getMoviesWithAvgGradeDesc(Pageable pageable) {
        return movieRepository.findAllMoviesWithAvgGradeDesc(pageable);
    }

    public Page<MovieWithAvgGradeDto> getMoviesWithAvgGradeAsc(Pageable pageable) {
        return movieRepository.findAllMoviesWithAvgGradeAsc(pageable);
    }

    public List<MovieWithAvgGradeDto> getTopTenMoviesWithAvgGrade() {
        return movieRepository.findTop10MoviesByAvgGrade();
    }


    public Page<MovieWithAvgGradeDto> getMoviesPropositionForUser(String userId, Pageable pageable) {
        var movies = watchedMoviesRepository.findMoviesByUserId(userId);
        List<String> categories = movies.stream().map(Movie::getCategory).distinct().toList();
        return movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies(categories, movies, pageable);
    }


    public boolean movieExists(Long id) {
        return movieRepository.existsById(id);
    }


    public MovieWithAvgGradeDto getMovieWithAvgGradeById(Long id) {
        var movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException("Movie does not exist"));
        return movieMapper.movieToMovieWithAvgGradeDto(movie);
    }

    public Page<MovieWithAvgGradeDto> getAllMoviesWithAvgGradeDto(Pageable pageable) {
        return movieRepository.findAll(pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> searchMoviesWithAvgGradeByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleIgnoreCaseContaining(title, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> findMoviesByCategoryWithAvgGrade(String category, Pageable pageable) {
        return movieRepository.findMoviesByCategory(category, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getMoviesByTagWithAvgGrade(String tag, Pageable pageable) {
        return movieRepository.findByTagContaining(tag, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getMoviesByPolishPremiereMonthAndYearWithAvgGrade(int month, int year, Pageable pageable) {
        return movieRepository.findMovieByPolishPremiereMonthAndYear(month, year, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }

    public Page<MovieWithAvgGradeDto> getMoviesByWorldPremiereMonthAndYearWithAvgGrade(int month, int year, Pageable pageable) {
        return movieRepository.findMovieByWorldPremiereMonthAndYear(month, year, pageable).map(movieMapper::movieToMovieWithAvgGradeDto);
    }


}











