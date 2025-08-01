package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.MovieGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieGradeRepository extends JpaRepository<MovieGrade, Long> {
    List<MovieGrade> findByMovieId(Long movieId);
}
