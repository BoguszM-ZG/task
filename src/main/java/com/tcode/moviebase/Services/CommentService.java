package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;

    public Comment addComment(String userId, Long movieId, String commentText) {
        var movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            var comment = new Comment();
            comment.setUserId(userId);
            comment.setMovie(movie);
            comment.setCommentText(commentText);
            return commentRepository.save(comment);
        }
        return null;
    }

    public List<Comment> getCommentsByMovieId(Long movieId) {
        return commentRepository.findAllByMovieId(movieId);
    }

    @Transactional
    public void deleteComment(String userId, Long commentId) {
        commentRepository.removeCommentByIdAndUserId(commentId, userId);
    }

    public boolean checkIfCommentExists(Long commentId) {
        return commentRepository.findById(commentId).isPresent();
    }

    public boolean existsByCommentIdAndUserId(Long commentId, String userId) {
        return commentRepository.existsByIdAndUserId(commentId, userId);
    }

    public Comment updateComment(Long commentId, String userId, String newCommentText) {
        var comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null && comment.getUserId().equals(userId)) {
            comment.setCommentText(newCommentText);
            return commentRepository.save(comment);
        }
        return null;
    }
}
