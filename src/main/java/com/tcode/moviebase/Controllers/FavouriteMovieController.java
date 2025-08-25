package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;


import com.tcode.moviebase.Services.FavouriteService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouriteMovieController {

    private final FavouriteService favouriteService;


    @Operation(summary = "Add a movie to favourites for adults", description = "Adds a movie to the user's favourites list. Requires authentication.")
    @PreAuthorize("hasRole('client_admin') or hasRole('client_user')")
    @PostMapping("/add/{movieId}")
    public ResponseEntity<?> addFavouriteMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {

        var userId = jwt.getClaimAsString("sub");
        var movie = favouriteService.addFavouriteMovie(userId, movieId);

        return ResponseEntity.ok(movie);
    }

    @Operation(summary = "Get favourite movies", description = "Retrieves the user's favourite movies with average grades. Requires authentication.")
    @GetMapping
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getFavouriteMovies(@AuthenticationPrincipal Jwt jwt,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = favouriteService.getFavouriteMovies(userId, pageable);

        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


    @Operation(summary = "Remove a movie from favourites", description = "Removes a movie from the user's favourites list. Requires authentication.")
    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<?> removeFavouriteMovie(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        var userId = jwt.getClaimAsString("sub");
        favouriteService.removeFavouriteMovie(userId, movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest")
    @Operation(summary = "Get favourite movies by creation date", description = "Retrieves the user's favourite movies ordered by creation date in descending order. Requires authentication.")
    @ResponseBody
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getFavouriteMoviesByCreatedAtChronology(@AuthenticationPrincipal Jwt jwt,
                                                                                              @RequestParam(defaultValue = "0") int page,
                                                                                           @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var userId = jwt.getClaimAsString("sub");
        var movies = favouriteService.getFavouriteMoviesByCreatedAtNewest(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/oldest")
    @Operation(summary = "Get favourite movies by creation date", description = "Retrieves the user's favourite movies ordered by creation date in ascending order. Requires authentication.")
    @ResponseBody
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getFavouriteMoviesByCreatedAtOldest(@AuthenticationPrincipal Jwt jwt,
                                                                                          @RequestParam(defaultValue = "0") int page,
                                                                                          @RequestParam(defaultValue = "10") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = favouriteService.getFavouriteMoviesByCreatedAtOldest(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/sortByTitleZ-A")
    @Operation(summary = "Get favourite movies sorted by title in descending order", description = "Retrieves the user's favourite movies sorted by title in descending order. Requires authentication.")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getFavouriteMoviesByTitleDesc(@AuthenticationPrincipal Jwt jwt,
                                                                                    @RequestParam(defaultValue = "0") int page,
                                                                                    @RequestParam(defaultValue = "10") int size) {
        var userId = jwt.getClaimAsString("sub");
        var pageable = PageRequest.of(page, size);
        var movies = favouriteService.getFavouriteMoviesByTitleZ_A(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/sortByTitleA-Z")
    @Operation(summary = "Get favourite movies sorted by title in ascending order", description = "Retrieves the user's favourite movies sorted by title in ascending order. Requires authentication.")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getFavouriteMoviesByTitleAsc(@AuthenticationPrincipal Jwt jwt,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var userId = jwt.getClaimAsString("sub");
        var movies = favouriteService.getFavouriteMoviesByTitleA_Z(userId, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/category")
    @Operation(summary = "Get favourite movies by category", description = "Retrieves the user's favourite movies filtered by category. Requires authentication.")
    public ResponseEntity<Page<MovieWithAvgGradeDto>> getFavouriteMoviesByCategory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String category,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size
            ) {
        var pageable = PageRequest.of(page, size);
        var userId = jwt.getClaimAsString("sub");
        var movies = favouriteService.getFavouriteMoviesByCategory(userId, category, pageable);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }


//    ///////////////////////////////////////
//    /// kids///
//    //////////////////////////////////////
//    @Operation(summary = "Add a movie to favourites for kids", description = "Adds a movie to the kid favourites list. Requires authentication.")
//    @PostMapping("/kids/add/{movieId}")
//    public ResponseEntity<?> addFavouriteMovieForKids(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
//        if (movieService.getMovieById(movieId) == null) {
//            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
//        }
//        if (movieService.getMovieById(movieId).getAgeRestriction() != 0) {
//            return ResponseEntity.badRequest().body("This movie is not suitable for kids");
//        }
//        if (favouriteService.existsFavouriteMovie(jwt.getClaimAsString("sub"), movieId)) {
//            return ResponseEntity.badRequest().body("Movie already in favourites");
//        }
//        var userId = jwt.getClaimAsString("sub");
//        var movie = favouriteService.addFavouriteMovie(userId, movieId);
//        var movieDto = new MovieWithAvgGradeDto(
//                movie.getTitle(),
//                movie.getMovie_year(),
//                movie.getCategory(),
//                movie.getDescription(),
//                movie.getPrizes(),
//                movie.getWorld_premiere(),
//                movie.getPolish_premiere(),
//                movie.getTag(),
//                movie.getAgeRestriction(),
//                movieGradeService.getAvgGrade(movieId)
//        );
//        return ResponseEntity.ok(movieDto);
//    }
//    ///////////////////////////
//    //////////juniors
//    ///////////////////////////
//    @Operation(summary = "Add a movie to favourites for juniors", description = "Adds a movie to the junior favourites list. Requires authentication.")
//    @PreAuthorize("hasRole('client_junior') or hasRole('client_admin') or hasRole('client_user')")
//    @PostMapping("/juniors/add/{movieId}")
//    public ResponseEntity<?> addFavouriteMovieForJuniors(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
//        if (movieService.getMovieById(movieId) == null) {
//            return ResponseEntity.badRequest().body("Movie not found with id: " + movieId);
//        }
//        if (movieService.getMovieById(movieId).getAgeRestriction() > 17) {
//            return ResponseEntity.badRequest().body("This movie is not suitable for juniors");
//        }
//        if (favouriteService.existsFavouriteMovie(jwt.getClaimAsString("sub"), movieId)) {
//            return ResponseEntity.badRequest().body("Movie already in favourites");
//        }
//        var userId = jwt.getClaimAsString("sub");
//        var movie = favouriteService.addFavouriteMovie(userId, movieId);
//        var movieDto = new MovieWithAvgGradeDto(
//                movie.getTitle(),
//                movie.getMovie_year(),
//                movie.getCategory(),
//                movie.getDescription(),
//                movie.getPrizes(),
//                movie.getWorld_premiere(),
//                movie.getPolish_premiere(),
//                movie.getTag(),
//                movie.getAgeRestriction(),
//                movieGradeService.getAvgGrade(movieId)
//        );
//        return ResponseEntity.ok(movieDto);
//    }


}
