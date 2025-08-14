package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByMovieId(Long movieId);


    boolean existsByIdAndUserId(Long id, String userId);

    void removeCommentByIdAndUserId(Long id, String userId);
}
