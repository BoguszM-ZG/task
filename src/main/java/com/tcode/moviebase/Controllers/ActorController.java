package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Dtos.ActorDto;
import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Services.ActorGradeService;
import com.tcode.moviebase.Services.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/actors")
public class ActorController {

    private final ActorService actorService;
    private final ActorGradeService actorGradeService;



    @Operation(summary = "Delete an actor", description = "Deletes an actor by its ID from the database.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "Add a new actor", description = "Adds a new actor to the database.")
    @PostMapping
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> addActor(@RequestBody Actor actor) {
        var savedActor = actorService.saveActor(actor);
        return ResponseEntity.ok().body(savedActor);
    }

    @Operation(summary = "Add a grade for an actor", description = "Adds a grade for an actor by its ID and returns average grade.")
    @PostMapping("/{actorId}/grade")
    public ResponseEntity<?> addActorGrade(@PathVariable Long actorId, @RequestParam int grade) {
        actorGradeService.addActorGrade(actorId, grade);

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
    public ResponseEntity<ActorDto> updateActor(@PathVariable Long id, @RequestBody Actor actor) {
        var updatedActor = actorService.updateActor(id, actor);
        return ResponseEntity.ok(updatedActor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDto> getActorDtoById(@PathVariable Long id){
        var actorDto = actorService.getActorDtoById(id);
        return ResponseEntity.ok(actorDto);
    }

    @GetMapping
    public ResponseEntity<Page<ActorDto>> getAllActors(@RequestParam (defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var actorsDto = actorService.getActorsDto(pageable);
        if (actorsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(actorsDto);

    }

}
