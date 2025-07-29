package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    void deleteMovieById(Long id);

    @EntityGraph(attributePaths = "movieGrades")
    List<Movie> findAll();

    List<Movie> findByTitleIgnoreCaseContaining(String title);
    List<Movie> findMoviesByCategory(String category);
}
