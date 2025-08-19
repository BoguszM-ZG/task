package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Services.ForumService;
import com.tcode.moviebase.Services.ForumThreadService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/threads")
public class ForumThreadController {
    private final ForumThreadService forumThreadService;
    private final ForumService forumService;

    @Operation(summary = "Create a new forum thread", description = "Creates a new thread in the specified forum. The forum must exist.")
    @PostMapping("/create/{forumId}")
    public ResponseEntity<?> createThread(@PathVariable Long forumId,@RequestBody ForumThread forumThread) {
        if (forumService.getForumById(forumId) == null) {
            return ResponseEntity.badRequest().body("forum with that id does not exist");
        }
        var createdThread = forumThreadService.createThread(forumId, forumThread);
        return ResponseEntity.ok(createdThread);
    }

    @Operation(summary = "Get all forum threads", description = "Retrieves a list of all forum threads.")
    @GetMapping("/all")
    public ResponseEntity<?> getAllForumThreads() {
        var threads = forumThreadService.getAllForumThreads();
        if (threads.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(threads);
    }

    @Operation(summary = "Get a forum thread by ID", description = "Retrieves a specific forum thread by its ID.")
    @GetMapping("/forum/{forumId}")
    public ResponseEntity<?> getAllForumThreadsByForumId(@PathVariable Long forumId)
    {
        var threads = forumThreadService.getAllForumThreadsByForumId(forumId);
        if (threads.isEmpty()) {
            return ResponseEntity.badRequest().body("No threads found for this forum");
        }
        return ResponseEntity.ok(threads);
    }

    @Operation(summary = "delete a forum thread", description = "Deletes a specific forum thread by its ID. The thread must exist.")
    @DeleteMapping("/delete/{forumThreadId}")
    public ResponseEntity<?> deleteForumThread(@PathVariable Long forumThreadId) {
        if (forumThreadService.existsById(forumThreadId)) {
            forumThreadService.deleteForumThread(forumThreadId);
            return ResponseEntity.ok("Forum thread deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Forum thread with that id does not exist");
        }
    }
}
