//package com.tcode.moviebase.Controllers;
//
//
//import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
//import com.tcode.moviebase.Entities.Movie;
//import com.tcode.moviebase.Services.MovieGradeService;
//import com.tcode.moviebase.Services.MovieJuniorService;
//import com.tcode.moviebase.Services.MovieService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/movies/juniors")
//@RequiredArgsConstructor
//public class MovieJuniorController {
//    private final MovieJuniorService movieJuniorService;
//    private final MovieGradeService movieGradeService;
//    private final MovieService movieService;
//
//
//    @Operation(summary = "Get all movies suitable for juniors", description = "Returns a list of movies that are suitable for juniors, with an age restriction of 0.")
//    @GetMapping
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> getMoviesForJuniors() {
//        var movies = movieJuniorService.getMoviesForJuniors();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "get movie by id for juniors", description = "Returns a movie by its ID if it is suitable for juniors, otherwise returns a message indicating the movie is not suitable.")
//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<Optional<?>> getMovieByIdJuniors(@PathVariable Long id) {
//        var movie = movieJuniorService.getMovieByIdJuniors(id);
//        if (movie.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(movie);
//        }
//    }
//
//    @Operation(summary = "Get movies by category for juniors", description = "Returns a list of movies that are suitable for juniors in the specified category, with an age restriction of 17 and below.")
//    @GetMapping("/category/{category}")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> findByCategoryForJuniors(@PathVariable String category) {
//        var movies = movieJuniorService.findByCategoryForJuniors(category);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Search movies by title for juniors", description = "Returns a list of movies that are suitable for juniors and match the specified title, with an age restriction of 17 and below.")
//    @GetMapping("/findByTitle")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> getMoviesByTitleForJuniors(@RequestParam String title) {
//        var movies = movieJuniorService.searchForJuniors(title);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies with new tag for juniors", description = "Returns a list of movies that are suitable for juniors and match the specified tag, with an age restriction of 17 and below.")
//    @GetMapping("/findNew")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> getMoviesByTagForJuniors() {
//        String tag = "new";
//        var movies = movieJuniorService.findMoviesForJuniorsByTag(tag);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies by premiere year for juniors", description = "Returns a list of movies that are suitable for juniors and match the specified premiere year, with an age restriction of 17 and below.")
//    @GetMapping("/premiereYear")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> getMoviesByPremiereYearForJuniors(@RequestParam int premiereYear) {
//        var movies = movieJuniorService.getMoviesByPremiereYearForJuniors(premiereYear);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies by Polish premiere month and year for juniors", description = "Returns a list of movies that are suitable for juniors and match the specified Polish premiere month and year, with an age restriction of 17 and below.")
//    @GetMapping("/polishPremiere")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> getMoviesByPolishPremiereMonthAndYearForJuniors(@RequestParam int month, @RequestParam int year) {
//        var movies = movieJuniorService.getMoviesByPolishPremiereMonthAndYearForJuniors(month, year);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies by world premiere month and year for juniors", description = "Returns a list of movies that are suitable for juniors and match the specified world premiere month and year, with an age restriction of 17 and below.")
//    @GetMapping("/worldPremiere")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<Movie>> getMoviesByWorldPremiereMonthAndYearForJuniors(@RequestParam int month, @RequestParam int year) {
//        var movies = movieJuniorService.getMoviesByWorldPremiereMonthAndYearForJuniors(month, year);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies with average grade in descending order for juniors", description = "Returns a list of movies that are suitable for juniors, sorted by average grade in descending order, with an age restriction of 17 and below.")
//    @GetMapping("/avgGradeDesc")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<MovieWithAvgGradeDto>> getMoviesWithAvgGradeDescForJuniors() {
//        var movies = movieJuniorService.getMoviesWithAvgGradeDescForJuniors();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies with average grade in ascending order for juniors", description = "Returns a list of movies that are suitable for juniors, sorted by average grade in ascending order, with an age restriction of 17 and below.")
//    @GetMapping("/avgGradeAsc")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<MovieWithAvgGradeDto>> getMoviesWithAvgGradeAscForJuniors() {
//        var movies = movieJuniorService.getMoviesWithAvgGradeAscForJuniors();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get top ten movies with average grade for juniors", description = "Returns a list of the top ten movies that are suitable for juniors, sorted by average grade, with an age restriction of 17 and below.")
//    @GetMapping("/topTen")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<List<MovieWithAvgGradeDto>> getTopTenMoviesWithAvgGradeForJuniors() {
//        var movies = movieJuniorService.getTopTenMoviesWithAvgGradeForJuniors();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get juniors movie with average grade", description = "Retrieves a juniors movie (age restriction <= 17) along with its average grade by its ID.")
//    @GetMapping("/{id}/details")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<?> getJuniorsMovieWithAvgGrade(@PathVariable Long id) {
//        var movie = movieService.getMovieById(id);
//        if (movie == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (movie.getAgeRestriction() > 17) {
//            return ResponseEntity.status(403).body("You cannot access this movie, it is not suitable for juniors");
//        }
//        Double avgGrade = movieGradeService.getAvgGrade(id);
//
//        var movieWithAvgGradeDto = new MovieWithAvgGradeDto(
//                movie.getTitle(),
//                movie.getMovie_year(),
//                movie.getCategory(),
//                movie.getDescription(),
//                movie.getPrizes(),
//                movie.getWorld_premiere(),
//                movie.getPolish_premiere(),
//                movie.getTag(),
//                movie.getAgeRestriction(),
//                avgGrade
//        );
//        return ResponseEntity.ok(movieWithAvgGradeDto);
//    }
//    @Operation(summary = "Add a grade to a juniors movie", description = "Adds a grade to a juniors movie (age restriction <= 17) by movie ID and returns the average grade.")
//    @PostMapping("/{id}/grade")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<?> addJuniorsMovieGrade(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestParam int grade) {
//        var movie = movieService.getMovieById(id);
//        if (movie == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (movie.getAgeRestriction() > 17) {
//            return ResponseEntity.status(403).body("Movie is not for juniors.");
//        }
//        var userId = jwt.getClaimAsString("sub");
//        if(movieGradeService.existsGrade(jwt.getClaimAsString("sub"), id)) {
//            movieGradeService.updateGrade(userId, id, grade);
//            var avgGrade = movieGradeService.getAvgGrade(id);
//            return ResponseEntity.ok(avgGrade);
//        }
//
//        if (grade < 1 || grade > 10) {
//            return ResponseEntity.badRequest().body("Grade must be between 1 and 10.");
//        }
//
//
//
//        var movieGrade = movieGradeService.addGrade(userId, id, grade);
//        if (movieGrade == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            var avgGrade = movieGradeService.getAvgGrade(id);
//            return ResponseEntity.status(201).body(avgGrade);
//        }
//    }
//
//    @Operation(summary = "Get movie propositions for juniors", description = "Retrieves movie propositions based on the junior's watched movies.")
//    @GetMapping("/propositions")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    public ResponseEntity<?> getMoviePropositionsForJuniors(@AuthenticationPrincipal Jwt jwt){
//        String userId = jwt.getClaimAsString("sub");
//        if (userId == null) {
//            return ResponseEntity.badRequest().body("User ID is required.");
//        }
//        var movies = movieJuniorService.getMoviesPropositionForJuniors(userId);
//        if (movies.isEmpty()) {
//            var allMovies = movieJuniorService.getAllMoviesWithAvgGradeForJuniors();
//            return ResponseEntity.ok(allMovies);
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//
//}
