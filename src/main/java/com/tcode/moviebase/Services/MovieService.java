package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Movie;
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



    public Movie updateMovie(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id).orElse(null);

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setMovie_year(movie.getMovie_year());
        existingMovie.setCategory(movie.getCategory());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setPrizes(movie.getPrizes());
        existingMovie.setTag(movie.getTag());
        existingMovie.setWorld_premiere(movie.getWorld_premiere());
        existingMovie.setPolish_premiere(movie.getPolish_premiere());


        return movieRepository.save(existingMovie);
    }

    public List<Movie> getMoviesByTag(String tag) {
        return movieRepository.findByTagContaining(tag);
    }

    public List<Movie> getMoviesByPremiereYear(int premiereYear) {
        return movieRepository.findAll().stream()
                .filter(movie -> movie.getMovie_year() == premiereYear)
                .toList();
    }


    public List<Movie> getMoviesByPolishPremiereMonthAndYear(int month, int year) {
        return movieRepository.findMovieByPolishPremiereMonthAndYear(month, year);
    }

    public List<Movie> getMoviesByWorldPremiereMonthAndYear(int month, int year) {
        return movieRepository.findMovieByWorldPremiereMonthAndYear(month, year);
    }
}
