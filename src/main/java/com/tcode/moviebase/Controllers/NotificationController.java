package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Services.MovieService;
import com.tcode.moviebase.Services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final MovieService movieService;

    @Operation(summary = "Add notification for a movie", description = "Adds a notification for a specific movie if it does not already exist.")
    @PostMapping("/{movieId}")
    public ResponseEntity<?> addNotification(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if (movieService.getMovieById(movieId) == null) {
            return ResponseEntity.badRequest().body("Movie does not exist");
        }
        if(notificationService.existsNotification(jwt.getClaimAsString("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Notification already exists");
        }
        var userId = jwt.getClaimAsString("sub");
        notificationService.addNotification(userId, movieId);
        return ResponseEntity.ok("Notification added successfully");
    }

    @Operation(summary = "Remove notification for a movie", description = "Removes a notification for a specific movie if it exists.")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> removeNotification(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if(!notificationService.existsNotification(jwt.getClaimAsString("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Notification does not exist");
        }
        var userId = jwt.getClaimAsString("sub");
        notificationService.removeNotification(userId, movieId);
        return ResponseEntity.ok("Notification removed successfully");
    }


}
