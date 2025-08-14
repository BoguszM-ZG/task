package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Comment;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.CommentRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void testAddComment() {
        String userId = "user1";
        Long movieId = 1L;
        String commentText = "Great movie!";
        var movie = new Movie();
        var comment = new Comment();
        comment.setUserId(userId);
        comment.setMovie(movie);
        comment.setCommentText(commentText);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(commentRepository.save(org.mockito.ArgumentMatchers.any(Comment.class))).thenReturn(comment);

        var result = commentService.addComment(userId, movieId, commentText);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void testGetCommentsByMovieId() {
        Long movieId = 1L;
        var comments = List.of(new Comment());
        when(commentRepository.findAllByMovieId(movieId)).thenReturn(comments);

        var result = commentService.getCommentsByMovieId(movieId);
        assertEquals(comments, result);
    }

    @Test
    void testDeleteComment() {
        String userId = "user1";
        Long commentId = 1L;
        commentService.deleteComment(userId, commentId);
        verify(commentRepository).removeCommentByIdAndUserId(commentId, userId);
    }

    @Test
    void testCheckIfCommentExists() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));

        boolean exists = commentService.checkIfCommentExists(commentId);
        assertTrue(exists);
    }

    @Test
    void testExistsByCommentIdAndUserId() {
        Long commentId = 1L;
        String userId = "user1";
        when(commentRepository.existsByIdAndUserId(commentId, userId)).thenReturn(true);

        boolean exists = commentService.existsByCommentIdAndUserId(commentId, userId);
        assertTrue(exists);
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        String userId = "user1";
        String newCommentText = "Updated comment text";
        var comment = new Comment();
        comment.setUserId(userId);
        comment.setCommentText("Old comment text");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(org.mockito.ArgumentMatchers.any(Comment.class))).thenReturn(comment);

        var result = commentService.updateComment(commentId, userId, newCommentText);
        assertEquals(newCommentText, result.getCommentText());
    }

    @Test
    void testUpdateNonExistentComment() {
        Long commentId = 1L;
        String userId = "user1";
        String newCommentText = "Updated comment text";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        var result = commentService.updateComment(commentId, userId, newCommentText);
        assertNull(result);
    }

    @Test
    void testAddCommentWithNonExistentMovie() {
        String userId = "user1";
        Long movieId = 1L;
        String commentText = "Great movie!";

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        var result = commentService.addComment(userId, movieId, commentText);
        assertNull(result);
    }
}
