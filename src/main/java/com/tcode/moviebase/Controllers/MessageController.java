package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;


    @Operation(summary = "Send a message in a forum thread", description = "Allows a user to send a message in a specific forum thread. The user must be a member of the forum associated with the thread.")
    @PostMapping("/{forumId}/send/{threadId}")
    public ResponseEntity<?> addMessage(@AuthenticationPrincipal Jwt jwt, @PathVariable Long threadId, @RequestBody String content, @PathVariable Long forumId) {
        var userId = jwt.getClaimAsString("sub");
        var message = messageService.addMessage(userId, threadId, content, forumId);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Get all messages in a forum thread", description = "Retrieves all messages associated with a specific forum thread by its ID.")
    @GetMapping("/{threadId}")
    public ResponseEntity<?> getMessagesByThreadId(@PathVariable Long threadId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var messages = messageService.getMessagesByThreadId(threadId, pageable);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Delete a message", description = "Deletes a specific message by its ID. The message must exist.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id){
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }


}
