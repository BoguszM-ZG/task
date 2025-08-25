package com.tcode.moviebase.Repositories;


import com.tcode.moviebase.Entities.FavouriteMovie;
import com.tcode.moviebase.Entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface FavouriteMovieRepository extends JpaRepository<FavouriteMovie, Integer> {
    boolean existsByUserIdAndMovieId(String userId, Long movieId);
    void deleteByUserIdAndMovieId(String userId, Long movieId);

    @Query("SELECT fm.movie FROM FavouriteMovie fm WHERE fm.userId = :userId")
    Page<Movie> findMoviesByUserId(@Param("userId") String userId, Pageable pageable);



    @Query("SELECT fm.movie FROM FavouriteMovie fm WHERE fm.userId = :userID ORDER BY fm.createdAt DESC")
    Page<Movie> findMoviesByCreatedAt_Latest(@Param("userID") String userID, Pageable pageable);


    @Query("SELECT fm.movie FROM FavouriteMovie fm WHERE fm.userId = :userID ORDER BY fm.createdAt ASC")
    Page<Movie> findMoviesByCreatedAt_Oldest(@Param("userID") String userID, Pageable pageable);



    @Query("SELECT m FROM FavouriteMovie fm JOIN fm.movie m WHERE fm.userId = :userId ORDER BY m.title DESC")
    Page<Movie> findFavouriteMovieByTitleZ_A(@Param("userId") String userId, Pageable pageable);


    @Query("SELECT m FROM FavouriteMovie fm JOIN fm.movie m WHERE fm.userId = :userId ORDER BY m.title ASC")
    Page<Movie> findFavouriteMovieByTitleA_Z(@Param("userId") String userId, Pageable pageable);



    @Query("SELECT m FROM FavouriteMovie fm JOIN fm.movie m WHERE fm.userId = :userId AND m.category = :category")
    Page<Movie> findFavouriteMoviesByCategory(@Param("userId") String userId, @Param("category") String category, Pageable pageable);
}
