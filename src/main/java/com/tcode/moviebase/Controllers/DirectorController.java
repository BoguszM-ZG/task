package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Entities.Director;
import com.tcode.moviebase.Services.DirectorGradeService;
import com.tcode.moviebase.Services.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;
    private final DirectorGradeService directorGradeService;

    @Operation(summary = "Add a new director (client_admin role required)", description = "Adds a new director to the system. Requires client_admin role.")
    @PostMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addDirector(@RequestBody Director director) {
        var actorToAdd = directorService.addDirector(director);
        return ResponseEntity.ok(actorToAdd);
    }

    @Operation(summary = "Get director by ID", description = "Retrieves a director by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDirectorById(@PathVariable Long id) {
        var director = directorService.getDirectorById(id);
        return ResponseEntity.ok(director);
    }

    @Operation(summary = "Delete a director (client_admin role required)", description = "Deletes a director from the system by their ID. Requires client_admin role.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> deleteDirector(@PathVariable Long id) {
        directorService.getDirectorById(id);
        directorService.deleteDirector(id);
        return ResponseEntity.ok("Director deleted successfully.");
    }

    @Operation(summary = "Add or update director grade", description = "Allows an authenticated user to add or update their grade for a specific director.")
    @PostMapping("/{directorId}/grade")
    public ResponseEntity<?> addDirectorGrade(@AuthenticationPrincipal Jwt jwt, @PathVariable Long directorId, @RequestParam int grade) {
        var userId = jwt.getClaimAsString("sub");
        if (directorGradeService.existsDirectorGrade(userId, directorId)) {
            directorGradeService.updateDirectorGrade(userId, directorId, grade);
            var avgGrade = directorGradeService.getAvgDirectorGrade(directorId);
            return ResponseEntity.ok().body(avgGrade);
        }
        directorGradeService.addDirectorGrade(userId, directorId, grade);
        var avgGrade = directorGradeService.getAvgDirectorGrade(directorId);
        return ResponseEntity.ok().body(avgGrade);
    }

    @Operation(summary = "Add a movie to a director (client_admin role required)", description = "Associates a movie with a director. Requires client_admin role.")
    @PostMapping("/{directorId}/movies/{movieId}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addMovieToDirector(@PathVariable Long directorId, @PathVariable Long movieId) {
        directorService.addMovieToDirector(directorId, movieId);
        return ResponseEntity.ok("Movie added to director successfully.");
    }

    @Operation(summary = "Get directors by gender", description = "Retrieves a paginated list of directors filtered by gender")
    @GetMapping("/gender")
    public ResponseEntity<?> getDirectorsByGender(@RequestParam String gender,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var directorsDto = directorService.getDirectorsDtoByGender(gender, pageable);
        if (directorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directorsDto);
    }

    @Operation(summary = "Get directors sorted by last name", description = "Retrieves a paginated list of directors sorted by last name in ascending or ascending order.")
    @GetMapping("/sorted/A-Z")
    public ResponseEntity<?> getDirectorsDtoSortedByLastNameAsc(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var directorsDto = directorService.getDirectorsDtoSortedByLastNameAsc(pageable);
        if (directorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directorsDto);
    }

    @Operation(summary = "Get directors sorted by last name in descending order", description = "Retrieves a paginated list of directors sorted by last name in descending order.")
    @GetMapping("/sorted/Z-A")
    public ResponseEntity<?> getDirectorsSortedByLastNameDesc(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var directorsDto = directorService.getDirectorsDtoSortedByLastNameDesc(pageable);
        if (directorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directorsDto);
    }

    @Operation(summary = "Get directors by first name", description = "Retrieves a paginated list of directors filtered by first name.")
    @GetMapping("/firstName")
    public ResponseEntity<?> getDirectorsByFirstName(@RequestParam String firstName,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var directorsDto = directorService.getDirectorsByFirstName(firstName, pageable);
        if (directorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directorsDto);
    }

    @Operation(summary = "Get directors by last name", description = "Retrieves a paginated list of directors filtered by last name.")
    @GetMapping("/lastName")
    public ResponseEntity<?> getDirectorsByLastName(@RequestParam String lastName,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var directorsDto = directorService.getDirectorsByLastName(lastName, pageable);
        if (directorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directorsDto);
    }

    @Operation(summary = "Get all directors", description = "Retrieves a paginated list of all directors.")
    @GetMapping
    public ResponseEntity<?> getAllDirectors(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var directorsDto = directorService.getAllDirectors(pageable);
        if (directorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directorsDto);
    }



}
