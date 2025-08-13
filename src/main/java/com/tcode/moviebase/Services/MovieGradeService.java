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

    public MovieGrade addGrade(String userId, Long movieId, int grade) {

        Movie movie = movieRepository.findById(movieId).orElse(null);
        MovieGrade movieGrade = new MovieGrade();
        movieGrade.setUserId(userId);
        movieGrade.setMovie(movie);
        movieGrade.setGrade(grade);
        return movieGradeRepository.save(movieGrade);
    }

    public void updateGrade(String userId, Long movieId, int grade) {
        MovieGrade existingGrades = movieGradeRepository.findByUserIdAndMovieId(userId, movieId);
        if (existingGrades != null) {
            movieGradeRepository.delete(existingGrades);
        }
        addGrade(userId, movieId, grade);
    }

    public Double getAvgGrade(Long movieId) {
        List<MovieGrade> grades = movieGradeRepository.findByMovieId(movieId);
        if (grades.isEmpty()) {
            return 0.0;
        }

        var result = grades.stream().mapToDouble(MovieGrade::getGrade).sum();

        return result / grades.size();
    }

    public boolean existsGrade(String userId, Long movieId) {
        return movieGradeRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public Double getAvgGradeByUserId(String userId) {
        var grades = movieGradeRepository.findByUserId(userId);
        if (grades.isEmpty()) {
            return 0.0;
    }
        var result = grades.stream().mapToDouble(MovieGrade::getGrade).sum();
        return result / grades.size();
    }

    public Double getAvgGradeGivenYearAndMonth(String userId, int year, int month) {
        var grades = movieGradeRepository.findByUserIdInYearAndMonth(userId, year, month);
        if (grades.isEmpty()) {
            return 0.0;
        }
        var result = grades.stream().mapToDouble(MovieGrade::getGrade).sum();
        return result / grades.size();
    }

}
