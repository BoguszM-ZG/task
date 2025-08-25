package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Services.ToWatchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/to-watch")
@RequiredArgsConstructor
public class ToWatchController {
    private final ToWatchService toWatchService;



    @Operation(summary = "Get movies to watch", description = "Retrieves the user's movies to watch with average grades. Requires authentication.")
    @GetMapping
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getToWatchMovies(@AuthenticationPrincipal Jwt jwt,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = toWatchService.getToWatchMovies(userId, pageable);

        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Add a movie to watch list", description = "Adds a movie to the user's watch list. Requires authentication.")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    @PostMapping("/add/{movieId}")
    public ResponseEntity<?> addToWatchMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        var userId = jwt.getClaimAsString("sub");
        var movie = toWatchService.addToWatchMovie(userId, movieId);
        return ResponseEntity.ok(movie);
    }

    @Operation(summary = "Remove a movie from watch list", description = "Removes a movie from the user's watch list. Requires authentication.")
    @DeleteMapping("/remove/{movieId}")
    public ResponseEntity<?> removeToWatchMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {

        var userId = jwt.getClaimAsString("sub");
        toWatchService.removeToWatchMovie(userId, movieId);
        return ResponseEntity.ok("Movie removed from to watch list");
    }

    @Operation(summary = "Get movies to watch sorted by creation date (newest first)", description = "Retrieves the user's movies to watch sorted by creation date, newest first. Requires authentication.")
    @GetMapping("/latest")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getToWatchMoviesByCreatedAtNewest(@AuthenticationPrincipal Jwt jwt,
                                                                                        @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "20") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = toWatchService.getToWatchMoviesByCreatedAtNewest(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);

    }

    @Operation(summary = "Get movies to watch sorted by creation date (oldest first)", description = "Retrieves the user's movies to watch sorted by creation date, oldest first. Requires authentication.")
    @GetMapping("/oldest")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getToWatchMoviesByCreatedAtOldest(@AuthenticationPrincipal Jwt jwt,
                                                                                        @RequestParam(defaultValue = "0") int page,
                                                                                        @RequestParam(defaultValue = "20") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = toWatchService.getToWatchMoviesByCreatedAtOldest(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Get movies to watch sorted by title (Z to A)", description = "Retrieves the user's movies to watch sorted by title in descending order. Requires authentication.")
    @GetMapping("/sortByTitleZ_A")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getToWatchMoviesByTitleZ_A(@AuthenticationPrincipal Jwt jwt,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "20") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = toWatchService.getToWatchMoviesByTitleZ_A(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Get movies to watch sorted by title (A to Z)", description = "Retrieves the user's movies to watch sorted by title in ascending order. Requires authentication.")
    @GetMapping("/sortByTitleA_Z")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getToWatchMoviesByTitleA_Z(@AuthenticationPrincipal Jwt jwt,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "20") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = toWatchService.getToWatchMoviesByTitleA_Z(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Get movies to watch by category", description = "Retrieves the user's movies to watch filtered by category. Requires authentication.")
    @GetMapping("/category")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getToWatchMoviesByCategory(@AuthenticationPrincipal Jwt jwt, @RequestParam String category ,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "20") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = toWatchService.getToWatchMoviesByCategory(userId, category, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


//    ////////////////////
//    /////kids
//    ////////////////////
//
//    @Operation(summary = "Add a movie to kid's watch list", description = "Adds a movie to the kids's watch list. Requires authentication.")
//    @PostMapping("/kids/add/{movieId}")
//    public ResponseEntity<?> addToWatchMovieForKids(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
//        if (movieService.getMovieById(movieId) == null) {
//            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
//        }
//        if (movieService.getMovieById(movieId).getAgeRestriction() != 0) {
//            return ResponseEntity.badRequest().body("Movie is not suitable for kids");
//        }
//        if (toWatchService.existsToWatchMovie(jwt.getClaimAsString("sub"), movieId)) {
//            return ResponseEntity.badRequest().body("Movie already in watch list");
//        }
//        var userId = jwt.getClaimAsString("sub");
//        var movie = toWatchService.addToWatchMovie(userId, movieId);
//        if (movie == null) {
//            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
//        }
//        var movieDto = new MovieWithAvgGradeDto(
//                movie.getTitle(),
//                movie.getMovie_year(),
//                movie.getCategory(),
//                movie.getDescription(),
//                movie.getPrizes(),
//                movie.getWorld_premiere(),
//                movie.getPolish_premiere(),
//                movie.getTag(),
//                movie.getAgeRestriction(),
//                movieGradeService.getAvgGrade(movie.getId())
//        );
//        return ResponseEntity.ok(movieDto);
//    }
//
//    ///////////////
//    /// juniors
//    ////////////////
//    @Operation(summary = "Add a movie to junior's watch list", description = "Adds a movie to the junior's watch list. Requires authentication.")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    @PostMapping("/juniors/add/{movieId}")
//    public ResponseEntity<?> addToWatchMovieForJuniors(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
//        if (movieService.getMovieById(movieId) == null) {
//            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
//        }
//        if (movieService.getMovieById(movieId).getAgeRestriction() > 17) {
//            return ResponseEntity.badRequest().body("Movie is not suitable for juniors");
//        }
//        if (toWatchService.existsToWatchMovie(jwt.getClaimAsString("sub"), movieId)) {
//            return ResponseEntity.badRequest().body("Movie already in watch list");
//        }
//        var userId = jwt.getClaimAsString("sub");
//        var movie = toWatchService.addToWatchMovie(userId, movieId);
//        if (movie == null) {
//            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
//        }
//        var movieDto = new MovieWithAvgGradeDto(
//                movie.getTitle(),
//                movie.getMovie_year(),
//                movie.getCategory(),
//                movie.getDescription(),
//                movie.getPrizes(),
//                movie.getWorld_premiere(),
//                movie.getPolish_premiere(),
//                movie.getTag(),
//                movie.getAgeRestriction(),
//                movieGradeService.getAvgGrade(movie.getId())
//        );
//        return ResponseEntity.ok(movieDto);
//    }


}
