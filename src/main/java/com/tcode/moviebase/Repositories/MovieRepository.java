package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    void deleteMovieById(Long id);

    @EntityGraph(attributePaths = { "movieGrades", "actors" })
    List<Movie> findAll();

    @EntityGraph(attributePaths = "movieGrades")
    List<Movie> findByTitleIgnoreCaseContaining(String title);

    @EntityGraph(attributePaths = "movieGrades")
    List<Movie> findMoviesByCategory(String category);


    @Query("SELECT m FROM Movie m WHERE FUNCTION('MONTH', m.polish_premiere) = :month AND FUNCTION('YEAR', m.polish_premiere) = :year")
    List<Movie> findMovieByPolishPremiereMonthAndYear(int month, int year);


    @Query("SELECT m FROM Movie m WHERE FUNCTION('MONTH', m.world_premiere) = :month AND FUNCTION('YEAR', m.world_premiere) = :year")
    List<Movie> findMovieByWorldPremiereMonthAndYear(int month, int year);

    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction,AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction " +
            "HAVING AVG(g.grade) IS NOT NULL " +
            "ORDER BY AVG(g.grade) DESC ")
    List<MovieWithAvgGradeDto> findAllMoviesWithAvgGradeDesc();


    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction, AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag ,m.ageRestriction " +
            "HAVING AVG(g.grade) IS NOT NULL " +
            "ORDER BY AVG(g.grade) ASC ")
    List<MovieWithAvgGradeDto> findAllMoviesWithAvgGradeAsc();



    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction,AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction " +
            "HAVING AVG(g.grade) IS NOT NULL " +
            "ORDER BY AVG(g.grade) DESC LIMIT 10")
    List<MovieWithAvgGradeDto> findTop10MoviesByAvgGrade();





    List<Movie> findByTagContaining(String tag);
}
