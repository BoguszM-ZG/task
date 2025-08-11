package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;


import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Services.MovieGradeService;
import com.tcode.moviebase.Services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieGradeService movieGradeService;
    private final MovieRepository movieRepository;

    @Operation(summary = "Get all movies", description = "Retrieves a list of all movies in the database.")
    @GetMapping
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<Movie>> getAllMovies() {
        var movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Get a movie by ID", description = "Retrieves a movie by its ID from the database.")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        var movie = movieService.getMovieById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }

    @Operation(summary = "Add a new movie", description = "Adds a new movie to the database.")
    @PostMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        if (movie.getTitle() == null || movie.getMovie_year() == null || movie.getAgeRestriction() == null) {
            return ResponseEntity.badRequest().body("Title, year and age restriction are required fields.");
        }

        var movieAdded = movieService.addMovie(movie);
        return ResponseEntity.status(201).body(movieAdded);
    }


    @Operation(summary = "Delete a movie", description = "Deletes a movie by its ID from the database.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        var exists = movieService.getMovieById(id);
        if (exists == null) {
            return ResponseEntity.notFound().build();
        }
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get movies by category", description = "Retrieves a list of movies by their category.")
    @GetMapping("/findByCategory/{category}")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<Movie>> getMoviesByCategory(@PathVariable String category) {
        var movies = movieService.findByCategory(category);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Search movies by title", description = "Searches for movies by their title.")
    @GetMapping("/findByTitle")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@RequestParam String title) {
        var movies = movieService.search(title);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Add a grade to a movie", description = "Adds a grade to a movie by movie ID and returns the average grade.")
    @PostMapping("/{id}/grade")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<?> addMovieGrade(@PathVariable Long id, @RequestParam int grade) {
        if (grade < 1 || grade > 10) {
            return ResponseEntity.badRequest().body("Grade must be between 1 and 10.");
        }
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        var movieGrade = movieGradeService.addGrade(id, grade);
        if (movieGrade == null) {
            return ResponseEntity.notFound().build();
        } else {
            var avgGrade = movieGradeService.getAvgGrade(id);
            return ResponseEntity.status(201).body(avgGrade);
        }
    }

    @Operation(summary = "Update a movie", description = "Updates an existing movie by its ID.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        var newMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(newMovie);


    }

    @Operation(summary = "Get average grade of a movie", description = "Retrieves the average grade of a movie by its ID.")
    @GetMapping("/{id}/average-grade")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<Double> getAverageGrade(@PathVariable Long id) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        var avgGrade = movieGradeService.getAvgGrade(id);
        if (avgGrade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avgGrade);
    }

    @Operation(summary = "Get movie with average grade", description = "Retrieves a movie along with its average grade by its ID.")
    @GetMapping("/{id}/details")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<MovieWithAvgGradeDto> getMovieWithAvgGrade(@PathVariable Long id) {
        var movie = movieService.getMovieById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        Double avgGrade = movieGradeService.getAvgGrade(id);

        var movieWithAvgGradeDto = new MovieWithAvgGradeDto(
                movie.getTitle(),
                movie.getMovie_year(),
                movie.getCategory(),
                movie.getDescription(),
                movie.getPrizes(),
                movie.getWorld_premiere(),
                movie.getPolish_premiere(),
                movie.getTag(),
                movie.getAgeRestriction(),
                avgGrade
        );
        return ResponseEntity.ok(movieWithAvgGradeDto);
    }


    @Operation(summary = "Get new movies", description = "Retrieves a list of movies that contain a specific tag.")
    @GetMapping("/findNew")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<Movie>> getMoviesByTag() {
        String tag = "new";
        var movies = movieService.getMoviesByTag(tag);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies by premiere year", description = "Retrieves a list of movies that premiered in a specific year.")
    @GetMapping("/premiereYear")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<Movie>> getMoviesByPremiereYear(@RequestParam int premiereYear) {
        var movies = movieService.getMoviesByPremiereYear(premiereYear);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }


    @Operation(summary = "Get movies by polish premiere month and year", description = "Retrieves a list of movies that premiered in a specific month and year.")
    @GetMapping("/polishPremiereMonthAndYear")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<?> getMoviesByPolishPremiereMonthAndYear(@RequestParam int month, @RequestParam int year) {
        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest().body("Month must be between 1 and 12.");
        }
        var movies = movieService.getMoviesByPolishPremiereMonthAndYear(month, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies by world premiere month and year", description = "Retrieves a list of movies that premiered worldwide in a specific month and year.")
    @GetMapping("/worldPremiereMonthAndYear")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<Movie>> getMoviesByWorldPremiereMonthAndYear(@RequestParam int month, @RequestParam int year) {
        var movies = movieService.getMoviesByWorldPremiereMonthAndYear(month, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get all movies sorted by avg grades desc", description = "Retrieves a list of all movies sorted by their average grades in descending order.")
    @GetMapping("/sortedByAvgGradeDesc")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<MovieWithAvgGradeDto>> getAllMoviesSortedByAvgGradeDesc() {
        var movies = movieService.getMoviesWithAvgGradeDesc();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);

        }
    }
    @Operation(summary = "Get all movies sorted by avg grades asc", description = "Retrieves a list of all movies sorted by their average grades in ascending order.")
    @GetMapping("/sortedByAvgGradeAsc")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    public ResponseEntity<List<MovieWithAvgGradeDto>> getAllMoviesSortedByAvgGradeAsc() {
        var movies = movieService.getMoviesWithAvgGradeAsc();
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







}
