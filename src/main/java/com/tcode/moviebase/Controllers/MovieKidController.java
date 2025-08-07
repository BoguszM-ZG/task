package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Services.MovieKidService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies/kids")
@RequiredArgsConstructor
public class MovieKidController {
    private final MovieRepository movieRepository;
    private final MovieKidService movieKidService;


    @Operation(summary = "Get all movies suitable for kids", description = "Returns a list of movies that are suitable for kids, with an age restriction of 0.")
    @GetMapping
    public ResponseEntity<List<Movie>> getMoviesForKids() {
        var movies = movieKidService.getMoviesForKids();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "get movie by id for kids", description = "Returns a movie by its ID if it is suitable for kids, otherwise returns a message indicating the movie is not suitable.")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<?>> getMovieByIdKids(@PathVariable Long id) {
        var movie = movieKidService.getMovieByIdKids(id);
        if (movie.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(movie);
        }
    }

    @Operation(summary = "Get movies by category for kids", description = "Returns a list of movies that are suitable for kids in the specified category, with an age restriction of 0.")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Movie>> findByCategoryForKids(@PathVariable String category) {
        var movies = movieKidService.findByCategoryForKids(category);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Search movies by title for kids", description = "Returns a list of movies that are suitable for kids and match the specified title, with an age restriction of 0.")
    @GetMapping("/findByTitle")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@RequestParam String title) {
        var movies = movieKidService.searchForKids(title);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies with new tag for kids", description = "Returns a list of movies that are suitable for kids and match the specified tag, with an age restriction of 0.")
    @GetMapping("/findNew")
    public ResponseEntity<List<Movie>> getMoviesByTag() {
        String tag = "new";
        var movies = movieKidService.findMoviesForKidsByTag(tag);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies by premiere year for kids", description = "Returns a list of movies that are suitable for kids and match the specified premiere year, with an age restriction of 0.")
    @GetMapping("/premiereYear")
    public ResponseEntity<List<Movie>> getMoviesByPremiereYearForKids(@RequestParam int premiereYear) {
        var movies = movieKidService.getMoviesByPremiereYearForKids(premiereYear);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies by Polish premiere month and year for kids", description = "Returns a list of movies that are suitable for kids and match the specified Polish premiere month and year, with an age restriction of 0.")
    @GetMapping("/polishPremiere")
    public ResponseEntity<List<Movie>> getMoviesByPolishPremiereMonthAndYearForKids(@RequestParam int month, @RequestParam int year) {
        var movies = movieKidService.getMoviesByPolishPremiereMonthAndYearForKids(month, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @Operation(summary = "Get movies by world premiere month and year for kids", description = "Returns a list of movies that are suitable for kids and match the specified world premiere month and year, with an age restriction of 0.")
    @GetMapping("/worldPremiere")
    public ResponseEntity<List<Movie>> getMoviesByWorldPremiereMonthAndYearForKids(@RequestParam int month, @RequestParam int year) {
        var movies = movieKidService.getMoviesByWorldPremiereMonthAndYearForKids(month, year);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

}
