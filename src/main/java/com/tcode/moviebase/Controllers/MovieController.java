package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;


import com.tcode.moviebase.Services.MovieGradeService;
import com.tcode.moviebase.Services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieGradeService movieGradeService;





    @Operation(summary = "Add a new movie", description = "Adds a new movie to the database.")
    @PostMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        var movieAdded = movieService.addMovie(movie);
        return ResponseEntity.status(201).body(movieAdded);
    }


    @Operation(summary = "Delete a movie", description = "Deletes a movie by its ID from the database.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get movie propositions for user", description = "Retrieves movie propositions based on the user's watched movies.")
    @GetMapping("/propositions")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<?> getMoviePropositionsForUser(@AuthenticationPrincipal Jwt jwt,

                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size){
        String userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = movieService.getMoviesPropositionForUser(userId, pageable);
        if (movies.isEmpty()) {
            var allMovies = movieService.getAllMoviesWithAvgGradeDto(pageable);
            return ResponseEntity.ok(allMovies);
        } else {
            return ResponseEntity.ok(movies);
        }
    }





    @Operation(summary = "Add a grade to a movie", description = "Adds a grade to a movie by movie ID and returns the average grade.")
    @PostMapping("/{id}/grade")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<?> addMovieGrade(@AuthenticationPrincipal Jwt jwt,@PathVariable Long id, @RequestParam int grade) {
        var userId = jwt.getClaimAsString("sub");
        if(movieGradeService.existsGrade(jwt.getClaimAsString("sub"), id)) {
            movieGradeService.updateGrade(userId, id, grade);
            var avgGrade = movieGradeService.getAvgGrade(id);
            return ResponseEntity.ok(avgGrade);
        }

        movieGradeService.addGrade(userId, id, grade);

        var avgGrade = movieGradeService.getAvgGrade(id);
        return ResponseEntity.status(201).body(avgGrade);

    }

    @Operation(summary = "Update a movie", description = "Updates an existing movie by its ID.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<MovieWithAvgGradeDto> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        var newMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(newMovie);
    }



    @Operation(summary = "Get average grade of a movie", description = "Retrieves the average grade of a movie by its ID.")
    @GetMapping("/{id}/average-grade")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<Double> getAverageGrade(@PathVariable Long id) {
        movieService.getMovieById(id);
        var avgGrade = movieGradeService.getAvgGrade(id);
        return ResponseEntity.ok(avgGrade);
    }


    @Operation(summary = "Get all movies sorted by avg grades desc", description = "Retrieves a list of all movies sorted by their average grades in descending order.")
    @GetMapping("/sortedByAvgGradeDesc")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getAllMoviesSortedByAvgGradeDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.getMoviesWithAvgGradeDesc(pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);

        }
    }
    @Operation(summary = "Get all movies sorted by avg grades asc", description = "Retrieves a list of all movies sorted by their average grades in ascending order.")
    @GetMapping("/sortedByAvgGradeAsc")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getAllMoviesSortedByAvgGradeAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.getMoviesWithAvgGradeAsc(pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get top 10 movies by average grade", description = "Retrieves the top 10 movies sorted by their average grades in descending order.")
    @GetMapping("/top10ByAvgGrade")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<MovieWithAvgGradeDto>> getTop10MoviesByAvgGrade()
    {
        var movies = movieService.getTopTenMoviesWithAvgGrade();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }



    @Operation(summary = "Get average grade by user ID", description = "Retrieves the average grade given by a user across all movies they have rated.")
    @GetMapping("/average-grade-by-user")
    public ResponseEntity<?> getAvgGradeByUserId(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("sub");
        Double avgGrade = movieGradeService.getAvgGradeByUserId(userId);
        if (avgGrade == 0.0) {
            return ResponseEntity.ok("You have not rated any movies yet.");
        }
        return ResponseEntity.ok(avgGrade);
    }

    @Operation(summary = "Get average grade by user ID in a specific year and month", description = "Retrieves the average grade given by a user in a specific year and month.")
    @GetMapping("/avg-grade-by-user-y-m")
    public ResponseEntity<?> getAvgGradeByUserIdInYearAndMonth(@AuthenticationPrincipal Jwt jwt, @RequestParam int year, @RequestParam int month) {
        String userId = jwt.getClaimAsString("sub");
        Double avgGrade = movieGradeService.getAvgGradeGivenYearAndMonth(userId, year, month);
        if (avgGrade == 0.0) {
            return ResponseEntity.ok("You have not rated any movies in this month and year.");
        }
        return ResponseEntity.ok(avgGrade);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovieWithAvgGradeDto> getMovieWithAvgGradeDto(@PathVariable Long id) {
        var movieWithAvgGrade = movieService.getMovieWithAvgGradeById(id);
        return ResponseEntity.ok(movieWithAvgGrade);
    }

    @GetMapping
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getAllMoviesWithAvgGradeDto(@RequestParam (defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "20") int size)
    {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.getAllMoviesWithAvgGradeDto(pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> searchMoviesWithAvgGradeByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.searchMoviesWithAvgGradeByTitle(title, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/findByCategory")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> findMoviesByCategoryWithAvgGrade(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.findMoviesByCategoryWithAvgGrade(category, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/findNew")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getMoviesByTagDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var tag = "new";
        var movies = movieService.getMoviesByTagWithAvgGrade(tag, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/polishPremiereMonthAndYear")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getMoviesByPolishPremiereMonthAndYearWithAvgGrade(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.getMoviesByPolishPremiereMonthAndYearWithAvgGrade(month, year, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/worldPremiereMonthAndYear")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getMoviesByWorldPremiereMonthAndYearWithAvgGrade(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var movies = movieService.getMoviesByWorldPremiereMonthAndYearWithAvgGrade(month, year, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }







}
