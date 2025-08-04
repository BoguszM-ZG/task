package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;


import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Services.MovieGradeService;
import com.tcode.moviebase.Services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Movie>> getAllMovies() {
        var movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @Operation(summary = "Add a new movie", description = "Adds a new movie to the database.")
    @PostMapping
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        if (movie.getTitle() == null || movie.getMovie_year() == null) {
            return ResponseEntity.badRequest().body("Title and year are required fields.");
        }

        var movieAdded = movieService.addMovie(movie);
        return ResponseEntity.status(201).body(movieAdded);
    }


    @Operation(summary = "Delete a movie", description = "Deletes a movie by its ID from the database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        var exists = movieService.getMovieById(id);
        if (exists == null) {
            return ResponseEntity.notFound().build();
        }
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a movie by ID", description = "Retrieves a movie by its ID from the database.")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        var movie = movieService.getMovieById(id);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }


    @Operation(summary = "Get movies by category", description = "Retrieves a list of movies by their category.")
    @GetMapping("/findByCategory/{category}")
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
    public ResponseEntity<?> addMovieGrade(@PathVariable Long id,@RequestParam int grade) {
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
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        var newMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(newMovie);


    }

    @Operation(summary = "Get average grade of a movie", description = "Retrieves the average grade of a movie by its ID.")
    @GetMapping("/{id}/average-grade")
    public ResponseEntity<Double> getAverageGrade(@PathVariable Long id) {
        var avgGrade = movieGradeService.getAvgGrade(id);
        if (avgGrade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avgGrade);
    }

    @Operation(summary = "Get movie with average grade", description = "Retrieves a movie along with its average grade by its ID.")
    @GetMapping("/{id}/details")
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
                avgGrade
        );
        return ResponseEntity.ok(movieWithAvgGradeDto);
    }


    @Operation(summary = "Get new movies", description = "Retrieves a list of movies that contain a specific tag.")
    @GetMapping("/findNew")
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
    public ResponseEntity<List<Movie>> getMoviesByPolishPremiereMonthAndYear(@RequestParam int month, @RequestParam int year) {
        var movies = movieService.getMoviesByPolishPremiereMonthAndYear(month, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies by world premiere month and year", description = "Retrieves a list of movies that premiered worldwide in a specific month and year.")
    @GetMapping("/worldPremiereMonthAndYear")
    public ResponseEntity<List<Movie>> getMoviesByWorldPremiereMonthAndYear(@RequestParam int month, @RequestParam int year) {
        var movies = movieService.getMoviesByWorldPremiereMonthAndYear(month, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }






}
