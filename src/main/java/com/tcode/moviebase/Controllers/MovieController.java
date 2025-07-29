package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Services.MovieService;
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

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        var movieAdded = movieService.addMovie(movie);
        return ResponseEntity.status(201).body(movieAdded);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        return movieRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByCategory/{category}")
    public ResponseEntity<List<Movie>> getMoviesByCategory(@PathVariable String category) {
        var movies = movieService.findByCategory(category);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@RequestParam String title) {
        List<Movie> movies = movieService.search(title);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @PostMapping("/{id}/grade")
    public ResponseEntity<MovieGrade> addMovieGrade(@PathVariable Long id, @RequestParam int grade) {
        var movieGrade = movieService.addGrade(id, grade);
        if (movieGrade == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(201).body(movieGrade);
        }
    }







}
