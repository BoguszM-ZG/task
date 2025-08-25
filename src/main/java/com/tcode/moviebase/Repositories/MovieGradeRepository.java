package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.MovieGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieGradeRepository extends JpaRepository<MovieGrade, Long> {
    List<MovieGrade> findByMovieId(Long movieId);

    boolean existsByUserIdAndMovieId(String userId, Long movieId);

    MovieGrade findByUserIdAndMovieId(String userId, Long movieId);

    List<MovieGrade> findByUserId(String userId);

    @Query("SELECT mg FROM MovieGrade mg WHERE mg.userId = :userId AND FUNCTION('YEAR', mg.createdAt) = :year AND FUNCTION('MONTH', mg.createdAt) = :month")
    List<MovieGrade> findByUserIdInYearAndMonth(String userId, int year, int month);
}
