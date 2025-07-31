package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Repositories.MovieGradeRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieGradeRepository movieGradeRepository;
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }


    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteMovieById(id);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<Movie> search(String search) {
        return movieRepository.findByTitleIgnoreCaseContaining(search);
    }

    public List<Movie> findByCategory(String category) {
        return movieRepository.findMoviesByCategory(category);
    }

    public MovieGrade addGrade(Long movieId, int grade) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        MovieGrade movieGrade = new MovieGrade();
        movieGrade.setMovie(movie);
        movieGrade.setGrade(grade);
        return movieGradeRepository.save(movieGrade);
    }

    public Movie updateMovie(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id).orElse(null);

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setYear(movie.getYear());
        existingMovie.setCategory(movie.getCategory());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setPrizes(movie.getPrizes());

        return movieRepository.save(existingMovie);
    }
}
