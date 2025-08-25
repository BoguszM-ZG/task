package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByMovieId(Long movieId, Pageable pageable);


    boolean existsByIdAndUserId(Long id, String userId);

    void removeCommentByIdAndUserId(Long id, String userId);
}
