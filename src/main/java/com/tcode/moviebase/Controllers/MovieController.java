package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public Iterable<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }


    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @GetMapping("/findByCategory/{category}")
    public List<Movie> getMoviesByCategory(@PathVariable String category) {
        return movieService.findByCategory(category);
    }

    @GetMapping("/findByTitle/{title}")
    public List<Movie> getMoviesByTitle(@PathVariable String title) {
        return movieService.search(title);
    }

    @PostMapping("/{id}/grade")
    public MovieGrade addMovieGrade(@PathVariable Long id, @RequestParam int grade) {
        return movieService.addGrade(id, grade);
    }







}
