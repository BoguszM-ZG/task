package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Services.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @Operation(summary = "Get all forums", description = "Retrieve a list of all forums")
    @GetMapping
    public ResponseEntity<List<Forum>> getAllForums() {
        if (forumService.getAllForums().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(forumService.getAllForums());
    }


    @Operation(summary = "Get forum by ID", description = "Retrieve a forum by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getForumById(@PathVariable Long id) {
        return forumService.getForumById(id) != null ?
            ResponseEntity.ok(forumService.getForumById(id)) :
            ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete forum", description = "delete a forum by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> deleteForum(@PathVariable Long id) {
        if (forumService.getForumById(id) == null) {
            return ResponseEntity.badRequest().body("forum not found");
        }
        forumService.deleteForum(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create forum", description = "Create a new forum")
    @PostMapping
    public ResponseEntity<Forum> createForum(@RequestBody Forum forum) {
        if (forum.getForumName() == null || forum.getForumName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Forum createdForum = forumService.addForum(forum);
        return ResponseEntity.status(201).body(createdForum);
    }


}
