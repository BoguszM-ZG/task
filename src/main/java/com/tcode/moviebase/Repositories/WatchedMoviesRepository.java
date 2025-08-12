package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.WatchedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WatchedMoviesRepository extends JpaRepository<WatchedMovie, Integer> {
    boolean existsByUserIdAndMovieId(String userId, Long movieId);

    void removeByUserIdAndMovieId(String userId, Long movieId);

    @Query("SELECT wm.movie FROM WatchedMovie wm WHERE wm.userId = :userId")
    List<Movie> findMoviesByUserId(String userId);
}
