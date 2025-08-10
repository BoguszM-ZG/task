package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Repositories.ActorRepository;
import com.tcode.moviebase.Services.ActorGradeService;
import com.tcode.moviebase.Services.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actors")
public class ActorController {

    private final ActorService actorService;
    private final ActorGradeService actorGradeService;
    private final ActorRepository actorRepository;


    @Operation(summary = "Get all actors", description = "Retrieves a list of all actors in the database.")
    @GetMapping
    public ResponseEntity<List<Actor>> findAllActors() {
        var actors = actorService.findAllActors();
        if (actors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(actors);
    }

    @Operation(summary = "Delete an actor", description = "Deletes an actor by its ID from the database.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        var exists = actorService.getActorById(id);
        if (exists == null) {
            return ResponseEntity.notFound().build();
        }
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get an actor by ID", description = "Retrieves an actor by its ID from the database.")
    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        var actor = actorService.getActorById(id);
        if (actor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actor);
    }

    @Operation(summary = "Add a new actor", description = "Adds a new actor to the database.")
    @PostMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addActor(@RequestBody Actor actor) {
        if (actor.getFirstName() == null || actor.getLastName() == null || actor.getGender() == null) {
            return ResponseEntity.badRequest().body("First name, last name and gender are required fields.");
        }

        var savedActor = actorService.saveActor(actor);
        return ResponseEntity.status(201).body(savedActor);
    }

    @Operation(summary = "Add a grade for an actor", description = "Adds a grade for an actor by its ID and returns average grade.")
    @PostMapping("/{actorId}/grade")
    public ResponseEntity<?> addActorGrade(@PathVariable Long actorId, @RequestParam int grade) {
        if (grade < 1 || grade > 10) {
            return ResponseEntity.badRequest().body("Grade must be between 1 and 10.");
        }

        if (!actorRepository.existsById(actorId)) {
            return ResponseEntity.notFound().build();
        }
        var actorGrade = actorGradeService.addActorGrade(actorId, grade);
        if (actorGrade == null) {
            return ResponseEntity.badRequest().body("Failed to add grade for actor.");
    }
        var avgGrade = actorGradeService.getAvgGrade(actorId);

        return ResponseEntity.status(201).body(avgGrade);
    }

    @Operation(summary = "Get average grade for an actor", description = "Retrieves the average grade for an actor by its ID.")
    @GetMapping("/{actorId}/average-grade")
    public ResponseEntity<Double> getAvgGrade(@PathVariable Long actorId) {
        var avgGrade = actorGradeService.getAvgGrade(actorId);
        if (avgGrade == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(avgGrade);
        }
    }


    @Operation(summary = "Update an actor", description = "Updates an existing actor by its ID.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Actor> updateActor(@PathVariable Long id, @RequestBody Actor actor) {
        if (!actorRepository.existsById(id)){
        return ResponseEntity.notFound().build();
        }
        var updatedActor = actorService.updateActor(id, actor);
        return ResponseEntity.ok(updatedActor);
    }

}
