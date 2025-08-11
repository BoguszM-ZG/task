package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.ToWatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToWatchRepository extends JpaRepository<ToWatch, Integer> {

    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId")
    List<Movie> findMoviesByUserId(@Param("userId") String userId);

    boolean existsByUserIdAndMovieId(String userId, Long movieId);

    void removeByUserIdAndMovieId(String userId, Long movieId);

    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId ORDER BY tw.createdAt DESC")
    List<Movie> findMoviesByCreatedAt_Latest(@Param("userId") String userId);

    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId ORDER BY tw.createdAt ASC")
    List<Movie> findMoviesByCreatedAt_Oldest(@Param("userId") String userId);

    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction, AVG(g.grade)) " +
            "FROM ToWatch tw " +
            "JOIN tw.movie m " +
            "LEFT JOIN m.movieGrades g " +
            "WHERE tw.userId = :userId " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction " +
            "ORDER BY m.title DESC")
    List<MovieWithAvgGradeDto> findMoviesByTitleZ_A(@Param("userId") String userId);

    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction, AVG(g.grade)) " +
            "FROM ToWatch tw " +
            "JOIN tw.movie m " +
            "LEFT JOIN m.movieGrades g " +
            "WHERE tw.userId = :userId " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction " +
            "ORDER BY m.title ASC")
    List<MovieWithAvgGradeDto> findMoviesByTitleA_Z(@Param("userId") String userId);


    @Query("SELECT new com.tcode.moviebase.Dtos.MovieWithAvgGradeDto(" +
            "m.title, m.movie_year, m.category, m.description, m.prizes, " +
            "m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction, AVG(g.grade)) " +
            "FROM ToWatch tw " +
            "JOIN tw.movie m " +
            "LEFT JOIN m.movieGrades g " +
            "WHERE tw.userId = :userId AND m.category = :category " +
            "GROUP BY m.title, m.movie_year, m.category, m.description, " +
            "m.prizes, m.world_premiere, m.polish_premiere, m.tag, m.ageRestriction")
    List<MovieWithAvgGradeDto> findMoviesByCategory(@Param("userId") String userId, @Param("category") String category);
}
