package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Dtos.CommentDto;
import com.tcode.moviebase.Services.CommentService;
import com.tcode.moviebase.Services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final MovieService movieService;


    @Operation(summary = "Add a comment to a movie", description = "add a comment to a movie by its ID. Requires user authentication.")
    @PostMapping("/add/{movieId}")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId,@RequestBody String commentText) {
        var movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        var userId = jwt.getClaimAsString("sub");
        if(userId == null)
        {
            return ResponseEntity.badRequest().body("You must be logged in to add a comment.");
        }
        var comment = commentService.addComment(userId, movieId, commentText);
        if (comment != null) {
            var commentDto = new CommentDto(
                    userId,
                    comment.getCommentText()
            );
            return ResponseEntity.ok(commentDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get comments by movie ID", description = "Retrieve all comments for a specific movie by its ID.")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getCommentsByMovieId(@PathVariable Long movieId) {
        var movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        var comments = commentService.getCommentsByMovieId(movieId);
        if (comments.isEmpty()) {
            return ResponseEntity.ok("No comments found for this movie.");
        } else {
            List<CommentDto> commentsDtos = comments.stream()
                    .map(comment -> new CommentDto(
                            comment.getUserId(),
                            comment.getCommentText()
                    )).toList();

            return ResponseEntity.ok(commentsDtos);
        }
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment by its ID. Requires user authentication and ownership of the comment.")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long commentId) {
        var userId = jwt.getClaimAsString("sub");
        if (userId == null) {
            return ResponseEntity.badRequest().body("You must be logged in to delete a comment.");
        }
        if (!commentService.checkIfCommentExists(commentId)) {
            return ResponseEntity.notFound().build();
        }
        if (!commentService.existsByCommentIdAndUserId(commentId, userId)) {
            return ResponseEntity.badRequest().body("You can only delete your own comments.");
        }
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }

    @Operation(summary = "Update a comment", description = "Update a comment by its ID. Requires user authentication and ownership of the comment.")
    @PutMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long commentId, @RequestBody String newCommentText) {
        var userId = jwt.getClaimAsString("sub");
        if (userId == null) {
            return ResponseEntity.badRequest().body("You must be logged in to update a comment.");
        }
        if (!commentService.checkIfCommentExists(commentId)) {
            return ResponseEntity.notFound().build();
        }
        if (!commentService.existsByCommentIdAndUserId(commentId, userId)) {
            return ResponseEntity.badRequest().body("You can only update your own comments.");
        }
        var updatedComment = commentService.updateComment(commentId, userId, newCommentText);
        if (updatedComment != null) {
            var commentDto = new CommentDto(
                    updatedComment.getUserId(),
                    updatedComment.getCommentText()
            );
            return ResponseEntity.ok(commentDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }






}






