package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Services.MovieGradeService;
import com.tcode.moviebase.Services.MovieService;
import com.tcode.moviebase.Services.WatchedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watched-movies")
@RequiredArgsConstructor
public class WatchedMoviesController {

    private final WatchedService watchedService;
    private final MovieService movieService;
    private final MovieGradeService movieGradeService;

    @Operation(summary = "Add a movie to watched movies", description = "Adds a movie to the user's watched movies list and remove that movie from to watch list. Returns the movie details with average grade if successful.")
    @PostMapping("/add/{movieId}")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<?> addWatchedMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if (movieService.getMovieById(movieId) == null) {
            return ResponseEntity.badRequest().body("Movie does not exist");
        }
        if (watchedService.existsWatchedMovie(jwt.getClaim("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Movie with is already in watched movies");
        }
        var userId = jwt.getClaimAsString("sub");
        var movie = watchedService.addWatchedMovie(userId, movieId);

        if (movie == null) {
            return ResponseEntity.badRequest().body("Failed to add movie to watched movies");
        }
        var movieDto = new MovieWithAvgGradeDto(
                movie.getTitle(),
                movie.getMovie_year(),
                movie.getCategory(),
                movie.getDescription(),
                movie.getPrizes(),
                movie.getWorld_premiere(),
                movie.getPolish_premiere(),
                movie.getTag(),
                movie.getAgeRestriction(),
                movieGradeService.getAvgGrade(movie.getId())
        );

        return ResponseEntity.ok(movieDto);

    }

    @Operation(summary = "Remove a movie from watched movies", description = "Removes a movie from the user's watched movies list. Returns a success message if successful.")
    @DeleteMapping("/remove/{movieId}")
    public ResponseEntity<?> removeWatchedMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if (movieService.getMovieById(movieId) == null) {
            return ResponseEntity.badRequest().body("Movie does not exist");
        }
        if (!watchedService.existsWatchedMovie(jwt.getClaim("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Movie is not in watched movies");
        }
        var userId = jwt.getClaimAsString("sub");
        watchedService.removeWatchedMovie(userId, movieId);
        return ResponseEntity.ok("Movie removed from watched movies successfully");
    }

    @Operation(summary = "Get all watched movies", description = "Retrieves all movies from the user's watched movies list with average grades.")
    @GetMapping
    public ResponseEntity<?> getWatchedMovies(@AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("sub");
        var movies = watchedService.getWatchedMovies(userId).stream()
                .map(movie -> new MovieWithAvgGradeDto(
                        movie.getTitle(),
                        movie.getMovie_year(),
                        movie.getCategory(),
                        movie.getDescription(),
                        movie.getPrizes(),
                        movie.getWorld_premiere(),
                        movie.getPolish_premiere(),
                        movie.getTag(),
                        movie.getAgeRestriction(),
                        movieGradeService.getAvgGrade(movie.getId())
                )).toList();

        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Add a movie to watched movies for kids", description = "Adds a movie to the kids's watched movies list and remove that movie from to watch list. Returns the movie details with average grade if successful.")
    @PostMapping("/kids/add/{movieId}")
    public ResponseEntity<?> addWatchedMovieForKids(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if (movieService.getMovieById(movieId) == null) {
            return ResponseEntity.badRequest().body("Movie does not exist");
        }
        if (watchedService.existsWatchedMovie(jwt.getClaim("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Movie with is already in watched movies");
        }
        if (movieService.getMovieById(movieId).getAgeRestriction() != 0) {
            return ResponseEntity.badRequest().body("Movie is not suitable for kids");
        }
        var userId = jwt.getClaimAsString("sub");
        var movie = watchedService.addWatchedMovie(userId, movieId);

        if (movie == null) {
            return ResponseEntity.badRequest().body("Failed to add movie to watched movies");
        }
        var movieDto = new MovieWithAvgGradeDto(
                movie.getTitle(),
                movie.getMovie_year(),
                movie.getCategory(),
                movie.getDescription(),
                movie.getPrizes(),
                movie.getWorld_premiere(),
                movie.getPolish_premiere(),
                movie.getTag(),
                movie.getAgeRestriction(),
                movieGradeService.getAvgGrade(movie.getId())
        );

        return ResponseEntity.ok(movieDto);

    }

    @Operation(summary = "Add a movie to watched movies for kids", description = "Adds a movie to the kids's watched movies list and remove that movie from to watch list. Returns the movie details with average grade if successful.")
    @PostMapping("/juniors/add/{movieId}")
    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<?> addWatchedMovieForJuniors(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if (movieService.getMovieById(movieId) == null) {
            return ResponseEntity.badRequest().body("Movie does not exist");
        }
        if (watchedService.existsWatchedMovie(jwt.getClaim("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Movie with is already in watched movies");
        }
        if (movieService.getMovieById(movieId).getAgeRestriction() > 17) {
            return ResponseEntity.badRequest().body("Movie is not suitable for juniors");
        }
        var userId = jwt.getClaimAsString("sub");
        var movie = watchedService.addWatchedMovie(userId, movieId);

        if (movie == null) {
            return ResponseEntity.badRequest().body("Failed to add movie to watched movies");
        }
        var movieDto = new MovieWithAvgGradeDto(
                movie.getTitle(),
                movie.getMovie_year(),
                movie.getCategory(),
                movie.getDescription(),
                movie.getPrizes(),
                movie.getWorld_premiere(),
                movie.getPolish_premiere(),
                movie.getTag(),
                movie.getAgeRestriction(),
                movieGradeService.getAvgGrade(movie.getId())
        );

        return ResponseEntity.ok(movieDto);

    }

    @Operation(summary = "Count watched movies", description = "Counts the number of movies in the user's watched movies list.")
    @GetMapping("/count")
    public ResponseEntity<?> countWatchedMovies(@AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("sub");
        var count = watchedService.countWatchedMovies(userId);
        return ResponseEntity.ok(count);
    }


    @Operation(summary = "Count watched movies in a specific month", description = "Counts the number of movies watched by the user in a specific month and year.")
    @GetMapping("/count-in-month")
    public ResponseEntity<?> countWatchedMoviesInMonth(@AuthenticationPrincipal Jwt jwt, @RequestParam int year, @RequestParam int month) {
        var userId = jwt.getClaimAsString("sub");
        var count = watchedService.countWatchedMoviesInMonth(userId, year, month);
        if (count == null) {
            return ResponseEntity.badRequest().body("Invalid year or month");
        }
        return ResponseEntity.ok(count);
    }
}
