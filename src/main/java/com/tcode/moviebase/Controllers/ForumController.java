package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Dtos.ForumNameDto;
import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Services.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @Operation(summary = "Get all forums", description = "Retrieve a list of all forums")
    @GetMapping
    public ResponseEntity<Page<ForumNameDto>> getAllForums(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        if (forumService.getAllForums(pageable).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(forumService.getAllForums(pageable));
    }


    @Operation(summary = "Get forum by ID", description = "Retrieve a forum by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getForumById(@PathVariable Long id) {
        return ResponseEntity.ok(forumService.getForumNameById(id));
    }

    @Operation(summary = "Delete forum", description = "delete a forum by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> deleteForum(@PathVariable Long id) {
        forumService.deleteForum(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create forum", description = "Create a new forum")
    @PostMapping
    public ResponseEntity<ForumNameDto> createForum(@RequestBody Forum forum) {
        var createdForum = forumService.addForum(forum);
        return ResponseEntity.ok().body(createdForum);
    }


}
