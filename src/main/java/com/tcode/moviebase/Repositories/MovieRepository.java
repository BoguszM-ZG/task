package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    void deleteMovieById(Long id);

    @EntityGraph(attributePaths = "movieGrades")
    List<Movie> findAll();

    @EntityGraph(attributePaths = "movieGrades")
    List<Movie> findByTitleIgnoreCaseContaining(String title);

    @EntityGraph(attributePaths = "movieGrades")
    List<Movie> findMoviesByCategory(String category);


    @Query("SELECT m FROM Movie m WHERE FUNCTION('MONTH', m.polish_premiere) = :month AND FUNCTION('YEAR', m.polish_premiere) = :year")
    List<Movie> findMovieByPolishPremiereMonthAndYear(int month, int year);


    @Query("SELECT m FROM Movie m WHERE FUNCTION('MONTH', m.world_premiere) = :month AND FUNCTION('YEAR', m.world_premiere) = :year")
    List<Movie> findMovieByWorldPremiereMonthAndYear(int month, int year);




    List<Movie> findByTagContaining(String tag);
}
