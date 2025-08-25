package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Services.CommentService;
import com.tcode.moviebase.Services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final MovieService movieService;


    @Operation(summary = "Add a comment to a movie", description = "add a comment to a movie by its ID. Requires user authentication.")
    @PostMapping("/add/{movieId}")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId,@RequestBody String commentText) {
        movieService.getMovieById(movieId);
        var userId = jwt.getClaimAsString("sub");
        return  ResponseEntity.ok(commentService.addComment(userId,movieId,commentText));
    }

    @Operation(summary = "Get comments by movie ID", description = "Retrieve all comments for a specific movie by its ID.")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getCommentsByMovieId(@PathVariable Long movieId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        movieService.getMovieById(movieId);
        var pageable = PageRequest.of(page, size);
        var comments = commentService.getCommentsByMovieId(movieId, pageable);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment by its ID. Requires user authentication and ownership of the comment.")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long commentId) {
        var userId = jwt.getClaimAsString("sub");
        commentService.getCommentById(commentId);
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok("Comment deleted successfully.");
    }

    @Operation(summary = "Update a comment", description = "Update a comment by its ID. Requires user authentication and ownership of the comment.")
    @PutMapping("/update/{commentId}")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal Jwt jwt, @PathVariable Long commentId, @RequestBody String newCommentText) {
        var userId = jwt.getClaimAsString("sub");
        return ResponseEntity.ok().body(commentService.updateComment(commentId, userId, newCommentText));

    }






}






