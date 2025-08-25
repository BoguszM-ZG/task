package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {


    @EntityGraph(attributePaths = { "movieGrades", "actors" })
    List<Movie> findAll();

    @EntityGraph(attributePaths = "movieGrades")
    Page<Movie> findByTitleIgnoreCaseContaining(String title, Pageable pageable);


    Page<Movie> findMoviesByCategory(String category, Pageable pageable);


    @Query("SELECT m FROM Movie m WHERE FUNCTION('MONTH', m.polish_premiere) = :month AND FUNCTION('YEAR', m.polish_premiere) = :year")
    Page<Movie> findMovieByPolishPremiereMonthAndYear(int month, int year, Pageable pageable);


    @Query("SELECT m FROM Movie m WHERE FUNCTION('MONTH', m.world_premiere) = :month AND FUNCTION('YEAR', m.world_premiere) = :year")
    Page<Movie> findMovieByWorldPremiereMonthAndYear(int month, int year, Pageable pageable);

    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction,AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction " +
            "HAVING AVG(g.grade) IS NOT NULL " +
            "ORDER BY AVG(g.grade) DESC ")
    Page<MovieWithAvgGradeDto> findAllMoviesWithAvgGradeDesc(Pageable pageable);





    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction, AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag ,m.ageRestriction " +
            "HAVING AVG(g.grade) IS NOT NULL " +
            "ORDER BY AVG(g.grade) ASC ")
    Page<MovieWithAvgGradeDto> findAllMoviesWithAvgGradeAsc(Pageable pageable);



    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction,AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction " +
            "HAVING AVG(g.grade) IS NOT NULL " +
            "ORDER BY AVG(g.grade) DESC LIMIT 10")
    List<MovieWithAvgGradeDto> findTop10MoviesByAvgGrade();





    Page<Movie> findByTagContaining(String tag, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.world_premiere = :worldPremiere")
    List<Movie> findMovieByWorld_premiere(LocalDate worldPremiere);

    @Query("SELECT m FROM Movie m WHERE m.polish_premiere = :polish_premiere")
    List<Movie> findMovieByPolish_premiere(LocalDate polish_premiere);

    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction, AVG(g.grade)) " +
            "FROM Movie m LEFT JOIN m.movieGrades g " +
            "WHERE m.category IN :categories AND m NOT IN :movies " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag ,m.ageRestriction ")
    Page<MovieWithAvgGradeDto> findMoviesPropositionByCategoriesDontIncludeWatchedMovies(List<String> categories, List<Movie> movies, Pageable pageable);


}
