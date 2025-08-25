package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.CommentDto;
import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Exceptions.CommentNotFoundException;
import com.tcode.moviebase.Exceptions.DeleteYourOwnCommentException;
import com.tcode.moviebase.Exceptions.ForbiddenWordsException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final MovieRepository movieRepository;
    private final CommentRepository commentRepository;
    private final ForbiddenWordService forbiddenWordService;

    public CommentDto addComment(String userId, Long movieId, String commentText) {
        var movie = movieRepository.findById(movieId).orElseThrow( () -> new MovieNotFoundException("Movie not found"));
        if (forbiddenWordService.containsForbiddenWords(commentText)){
            throw new ForbiddenWordsException("Your comment contains forbidden words");
        }
            var comment = new Comment();
            comment.setUserId(userId);
            comment.setMovie(movie);
            comment.setCommentText(commentText);
            commentRepository.save(comment);
            return commentToCommentDto(comment);
    }

    public Page<CommentDto> getCommentsByMovieId(Long movieId, Pageable pageable) {
        return commentRepository.findAllByMovieId(movieId, pageable).map(this::commentToCommentDto);
    }

    @Transactional
    public void deleteComment(String userId, Long commentId) {
        if (commentRepository.existsByIdAndUserId(commentId, userId)) {
            commentRepository.deleteById(commentId);
        }
        else {
            throw new DeleteYourOwnCommentException("Can only delete your own comments");
        }
    }

    public boolean checkIfCommentExists(Long commentId) {
        return commentRepository.findById(commentId).isPresent();
    }

    public CommentDto getCommentById(Long commentId) {
        return commentRepository.findById(commentId).map(this::commentToCommentDto).orElseThrow( () -> new CommentNotFoundException("Comment not found"));
    }


    public CommentDto updateComment(Long commentId, String userId, String newCommentText) {
        var comment = commentRepository.findById(commentId).orElseThrow( () -> new CommentNotFoundException("Comment not found"));
        if (commentRepository.existsByIdAndUserId(commentId, userId)) {
            comment.setCommentText(newCommentText);
            commentRepository.save(comment);
            return  commentToCommentDto(comment);
        }
        else {
            throw new DeleteYourOwnCommentException("Can only update your own comments");
        }
    }

    private CommentDto commentToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getUserId(),
                comment.getCommentText()
        );
    }
}
