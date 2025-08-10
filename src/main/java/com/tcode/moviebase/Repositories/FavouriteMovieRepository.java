package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.FavouriteMovie;
import com.tcode.moviebase.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteMovieRepository extends JpaRepository<FavouriteMovie, Integer> {
    boolean existsByUserIdAndMovieId(String userId, Long movieId);
    void deleteByUserIdAndMovieId(String userId, Long movieId);

    @Query("SELECT fm.movie FROM FavouriteMovie fm WHERE fm.userId = :userId")
    List<Movie> findMoviesByUserId(@Param("userId") String userId);


}
