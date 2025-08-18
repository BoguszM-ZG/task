package com.tcode.moviebase.Controllers;

import com.tcode.moviebase.Entities.Message;
import com.tcode.moviebase.Services.ForumMemberService;
import com.tcode.moviebase.Services.ForumThreadService;
import com.tcode.moviebase.Services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final ForumThreadService forumThreadService;
    private final ForumMemberService forumMemberService;


    @PostMapping("/send/{threadId}")
    public ResponseEntity<?> addMessage(@AuthenticationPrincipal Jwt jwt, @PathVariable Long threadId, @RequestBody String content) {
        var thread = forumThreadService.findById(threadId);
        if (thread == null) {
            return ResponseEntity.notFound().build();
        }
        var userId = jwt.getClaimAsString("sub");
        Long forumId = thread.getForum().getId();
        if (!forumMemberService.isMemberOfForum(userId, forumId)) {
            return ResponseEntity.badRequest().body("User is not a member of this forum");
        }
        Message message = messageService.addMessage(userId, threadId, content);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{threadId}")
    public ResponseEntity<?> getMessagesByThreadId(@PathVariable Long threadId) {
        var messages = messageService.getMessagesByThreadId(threadId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }


}
