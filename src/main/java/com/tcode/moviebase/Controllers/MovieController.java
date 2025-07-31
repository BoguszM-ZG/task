package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Repositories.MovieRepository;
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
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
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

    @Operation(summary = "Add a grade to a movie", description = "Adds a grade to a movie by movie ID.")
    @PostMapping("/{id}/grade")
    public ResponseEntity<MovieGrade> addMovieGrade(@PathVariable Long id, @RequestParam int grade) {
        var movieGrade = movieService.addGrade(id, grade);
        if (movieGrade == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(201).body(movieGrade);
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


}
