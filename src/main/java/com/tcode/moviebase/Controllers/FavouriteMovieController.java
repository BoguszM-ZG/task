package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;


import com.tcode.moviebase.Services.FavouriteService;
import com.tcode.moviebase.Services.MovieGradeService;
import com.tcode.moviebase.Services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouriteMovieController {

    private final FavouriteService favouriteService;
    private final MovieService movieService;
    private final MovieGradeService movieGradeService;


    @Operation(summary = "Add a movie to favourites", description = "Adds a movie to the user's favourites list. Requires authentication.")
    @PostMapping("/add/{movieId}")
    public ResponseEntity<?> addFavouriteMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if (movieService.getMovieById(movieId) == null) {
            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
        }
        if (favouriteService.existsFavouriteMovie(jwt.getClaimAsString("sub"), movieId)) {
            return ResponseEntity.badRequest().body("Movie already in favourites");
        }
        var userId = jwt.getClaimAsString("sub");
        var movie = favouriteService.addFavouriteMovie(userId, movieId);
        var movieDto = new MovieWithAvgGradeDto(
                movie.getTitle(),
                movie.getMovie_year(),
                movie.getCategory(),
                movie.getDescription(),
                movie.getPrizes(),
                movie.getWorld_premiere(),
                movie.getPolish_premiere(),
                movie.getTag(),
                movie.getAgeRestriction(),
                movieGradeService.getAvgGrade(movieId)
        );
        return ResponseEntity.ok(movieDto);
    }

    @Operation(summary = "Get favourite movies", description = "Retrieves the user's favourite movies with average grades. Requires authentication.")
    @GetMapping
    public ResponseEntity<List<MovieWithAvgGradeDto>> getFavouriteMovies(@AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("sub");
        var movies = favouriteService.getFavouriteMovies(userId).stream()
                .map(m -> new MovieWithAvgGradeDto(
                        m.getTitle(),
                        m.getMovie_year(),
                        m.getCategory(),
                        m.getDescription(),
                        m.getPrizes(),
                        m.getWorld_premiere(),
                        m.getPolish_premiere(),
                        m.getTag(),
                        m.getAgeRestriction(),
                        movieGradeService.getAvgGrade(m.getId())
                ))
                .toList();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


    @Operation(summary = "Remove a movie from favourites", description = "Removes a movie from the user's favourites list. Requires authentication.")
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<?> removeFavouriteMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        if(!favouriteService.existsFavouriteMovie(jwt.getClaimAsString("sub"), movieId)){
            return ResponseEntity.badRequest().body("Movie not found in favourites");
        }
        var userId = jwt.getClaimAsString("sub");
        favouriteService.removeFavouriteMovie(userId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest")
    @Operation(summary = "Get favourite movies by creation date", description = "Retrieves the user's favourite movies ordered by creation date in descending order. Requires authentication.")
    @ResponseBody
    public ResponseEntity<List<MovieWithAvgGradeDto>> getFavouriteMoviesByCreatedAtChronology(@AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("sub");
        var movies = favouriteService.getFavouriteMoviesByCreatedAtNewest(userId).stream()
                .map(m -> new MovieWithAvgGradeDto(
                        m.getTitle(),
                        m.getMovie_year(),
                        m.getCategory(),
                        m.getDescription(),
                        m.getPrizes(),
                        m.getWorld_premiere(),
                        m.getPolish_premiere(),
                        m.getTag(),
                        m.getAgeRestriction(),
                        movieGradeService.getAvgGrade(m.getId())
                ))
                .toList();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/oldest")
    @Operation(summary = "Get favourite movies by creation date", description = "Retrieves the user's favourite movies ordered by creation date in ascending order. Requires authentication.")
    @ResponseBody
    public ResponseEntity<List<MovieWithAvgGradeDto>> getFavouriteMoviesByCreatedAtOldest(@AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getClaimAsString("sub");
        var movies = favouriteService.getFavouriteMoviesByCreatedAtOldest(userId).stream()
                .map(m -> new MovieWithAvgGradeDto(
                        m.getTitle(),
                        m.getMovie_year(),
                        m.getCategory(),
                        m.getDescription(),
                        m.getPrizes(),
                        m.getWorld_premiere(),
                        m.getPolish_premiere(),
                        m.getTag(),
                        m.getAgeRestriction(),
                        movieGradeService.getAvgGrade(m.getId())
                ))
                .toList();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

}
