package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.MovieNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieNotificationRepository extends JpaRepository<MovieNotification, Integer> {
    boolean existsByUserIdAndMovieId(String userId, Long movieId);

    void deleteByUserIdAndMovieId(String userId, Long movieId);


    MovieNotification findByMovieId(Long movieId);


}
