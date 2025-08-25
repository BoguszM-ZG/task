package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.ToWatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToWatchRepository extends JpaRepository<ToWatch, Integer> {

    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId")
    Page<Movie> findMoviesByUserId(@Param("userId") String userId, Pageable pageable);

    boolean existsByUserIdAndMovieId(String userId, Long movieId);

    void removeByUserIdAndMovieId(String userId, Long movieId);

    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId ORDER BY tw.createdAt DESC")
    Page<Movie> findMoviesByCreatedAt_Latest(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId ORDER BY tw.createdAt ASC")
    Page<Movie> findMoviesByCreatedAt_Oldest(@Param("userId") String userId, Pageable pageable);



    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId ORDER BY tw.movie.title DESC")
    Page<Movie> findMoviesByTitleZ_A(@Param("userId") String userId, Pageable pageable);


    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId ORDER BY tw.movie.title ASC")
    Page<Movie> findMoviesByTitleA_Z(@Param("userId") String userId, Pageable pageable);




    @Query("SELECT tw.movie FROM ToWatch tw WHERE tw.userId = :userId AND tw.movie.category = :category")
    Page<Movie> findMoviesByCategory(String userId, String category, Pageable pageable);
}
