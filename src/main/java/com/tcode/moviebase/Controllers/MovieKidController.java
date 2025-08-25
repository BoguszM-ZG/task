//package com.tcode.moviebase.Controllers;
//
//
//import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
//import com.tcode.moviebase.Entities.Movie;
//import com.tcode.moviebase.Services.MovieGradeService;
//import com.tcode.moviebase.Services.MovieKidService;
//import com.tcode.moviebase.Services.MovieService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/movies/kids")
//@RequiredArgsConstructor
//public class MovieKidController {
//
//    private final MovieKidService movieKidService;
//    private final MovieService movieService;
//    private final MovieGradeService movieGradeService;
//
//
//    @Operation(summary = "Get all movies suitable for kids", description = "Returns a list of movies that are suitable for kids, with an age restriction of 0.")
//    @GetMapping
//    public ResponseEntity<List<Movie>> getMoviesForKids() {
//        var movies = movieKidService.getMoviesForKids();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "get movie by id for kids", description = "Returns a movie by its ID if it is suitable for kids, otherwise returns a message indicating the movie is not suitable.")
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<?>> getMovieByIdKids(@PathVariable Long id) {
//        var movie = movieKidService.getMovieByIdKids(id);
//        if (movie.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(movie);
//        }
//    }
//
//    @Operation(summary = "Get movies by category for kids", description = "Returns a list of movies that are suitable for kids in the specified category, with an age restriction of 0.")
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<Movie>> findByCategoryForKids(@PathVariable String category) {
//        var movies = movieKidService.findByCategoryForKids(category);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Search movies by title for kids", description = "Returns a list of movies that are suitable for kids and match the specified title, with an age restriction of 0.")
//    @GetMapping("/findByTitle")
//    public ResponseEntity<List<Movie>> getMoviesByTitleForKids(@RequestParam String title) {
//        var movies = movieKidService.searchForKids(title);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies with new tag for kids", description = "Returns a list of movies that are suitable for kids and match the specified tag, with an age restriction of 0.")
//    @GetMapping("/findNew")
//    public ResponseEntity<List<Movie>> getMoviesByTagForKids() {
//        String tag = "new";
//        var movies = movieKidService.findMoviesForKidsByTag(tag);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies by premiere year for kids", description = "Returns a list of movies that are suitable for kids and match the specified premiere year, with an age restriction of 0.")
//    @GetMapping("/premiereYear")
//    public ResponseEntity<List<Movie>> getMoviesByPremiereYearForKids(@RequestParam int premiereYear) {
//        var movies = movieKidService.getMoviesByPremiereYearForKids(premiereYear);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies by Polish premiere month and year for kids", description = "Returns a list of movies that are suitable for kids and match the specified Polish premiere month and year, with an age restriction of 0.")
//    @GetMapping("/polishPremiere")
//    public ResponseEntity<List<Movie>> getMoviesByPolishPremiereMonthAndYearForKids(@RequestParam int month, @RequestParam int year) {
//        var movies = movieKidService.getMoviesByPolishPremiereMonthAndYearForKids(month, year);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies by world premiere month and year for kids", description = "Returns a list of movies that are suitable for kids and match the specified world premiere month and year, with an age restriction of 0.")
//    @GetMapping("/worldPremiere")
//    public ResponseEntity<List<Movie>> getMoviesByWorldPremiereMonthAndYearForKids(@RequestParam int month, @RequestParam int year) {
//        var movies = movieKidService.getMoviesByWorldPremiereMonthAndYearForKids(month, year);
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies with average grade in descending order for kids", description = "Returns a list of movies that are suitable for kids, sorted by average grade in descending order, with an age restriction of 0.")
//    @GetMapping("/avgGradeDesc")
//    public ResponseEntity<List<MovieWithAvgGradeDto>> getMoviesWithAvgGradeDescForKids() {
//        var movies = movieKidService.getMoviesWithAvgGradeDescForKids();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get movies with average grade in ascending order for kids", description = "Returns a list of movies that are suitable for kids, sorted by average grade in ascending order, with an age restriction of 0.")
//    @GetMapping("/avgGradeAsc")
//    public ResponseEntity<List<MovieWithAvgGradeDto>> getMoviesWithAvgGradeAscForKids() {
//        var movies = movieKidService.getMoviesWithAvgGradeAscForKids();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get top ten movies with average grade for kids", description = "Returns a list of the top ten movies that are suitable for kids, sorted by average grade, with an age restriction of 0.")
//    @GetMapping("/topTen")
//    public ResponseEntity<List<MovieWithAvgGradeDto>> getTopTenMoviesWithAvgGradeForKids() {
//        var movies = movieKidService.getTopTenMoviesWithAvgGradeForKids();
//        if (movies.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//    @Operation(summary = "Get kids movie with average grade", description = "Retrieves a kids movie (age restriction 0) along with its average grade by its ID.")
//    @GetMapping("/{id}/details")
//    public ResponseEntity<?> getKidsMovieWithAvgGrade(@PathVariable Long id) {
//        var movie = movieService.getMovieById(id);
//        if (movie == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (movie.getAgeRestriction() != 0) {
//            return ResponseEntity.status(403).body("u cannot access this movie, it is not suitable for kids");
//        }
//        Double avgGrade = movieGradeService.getAvgGrade(id);
//
//        var movieWithAvgGradeDto = new MovieWithAvgGradeDto(movie.getTitle(), movie.getMovie_year(), movie.getCategory(), movie.getDescription(), movie.getPrizes(), movie.getWorld_premiere(), movie.getPolish_premiere(), movie.getTag(), movie.getAgeRestriction(), avgGrade);
//        return ResponseEntity.ok(movieWithAvgGradeDto);
//    }
//
//    @Operation(summary = "Add a grade to a kids movie", description = "Adds a grade to a kids movie (age restriction 0) by movie ID and returns the average grade.")
//    @PostMapping("/{id}/grade")
//    public ResponseEntity<?> addKidsMovieGrade(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestParam int grade) {
//        var movie = movieService.getMovieById(id);
//        if (movie == null) {
//            return ResponseEntity.badRequest().body("Movie not found.");
//        }
//        if (movie.getAgeRestriction() != 0) {
//            return ResponseEntity.status(403).body("Movie is not for kids.");
//        }
//        if (grade < 1 || grade > 10) {
//            return ResponseEntity.badRequest().body("Grade must be between 1 and 10.");
//        }
//        var userId = jwt.getClaimAsString("sub");
//        if (movieGradeService.existsGrade(jwt.getClaimAsString("sub"), id)) {
//            movieGradeService.updateGrade(userId, id, grade);
//            var avgGrade = movieGradeService.getAvgGrade(id);
//            return ResponseEntity.ok(avgGrade);
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
//    @Operation(summary = "Get movie propositions for kids", description = "Retrieves movie propositions based on the kids's watched movies.")
//    @GetMapping("/propositions")
//    public ResponseEntity<?> getMoviePropositionsForKids(@AuthenticationPrincipal Jwt jwt) {
//        String userId = jwt.getClaimAsString("sub");
//        if (userId == null) {
//            return ResponseEntity.badRequest().body("User ID is required.");
//        }
//        var movies = movieKidService.getMoviesPropositionForKids(userId);
//        if (movies.isEmpty()) {
//            var allMovies = movieKidService.getAllMoviesForKidsWithAvgGrade();
//            return ResponseEntity.ok(allMovies);
//        } else {
//            return ResponseEntity.ok(movies);
//        }
//    }
//
//}
