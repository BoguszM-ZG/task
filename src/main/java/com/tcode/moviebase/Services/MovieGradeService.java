package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Repositories.MovieGradeRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieGradeService {
    private final MovieGradeRepository movieGradeRepository;
    private final MovieRepository movieRepository;

    public MovieGrade addGrade(Long movieId, int grade) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        MovieGrade movieGrade = new MovieGrade();
        movieGrade.setMovie(movie);
        movieGrade.setGrade(grade);
        return movieGradeRepository.save(movieGrade);
    }

    public Double getAvgGrade(Long movieId) {
        List<MovieGrade> grades = movieGradeRepository.findByMovieId(movieId);
        if (grades.isEmpty()) {
            return 0.0;
        }

        var result = grades.stream().mapToDouble(MovieGrade::getGrade).sum();

        return result / grades.size();

    }

}
