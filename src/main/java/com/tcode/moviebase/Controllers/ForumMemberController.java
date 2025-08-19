package com.tcode.moviebase.Controllers;


import com.tcode.moviebase.Services.ForumMemberService;
import com.tcode.moviebase.Services.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/forum/member")
public class ForumMemberController {

    private final ForumMemberService forumMemberService;
    private final ForumService forumService;

    @Operation(summary = "Join a forum", description = "Allows a user to join a forum by its ID. The user must not already be a member of the forum.")
    @PostMapping("/join/{forumId}")
    public ResponseEntity<?> joinForum(@AuthenticationPrincipal Jwt jwt,@PathVariable Long forumId) {
        var userId = jwt.getClaimAsString("sub");
        if (forumService.getForumById(forumId) == null) {
            return ResponseEntity.badRequest().body("Forum with that ID does not exist");
        }
        if (forumMemberService.isMemberOfForum(userId, forumId)) {
            return ResponseEntity.badRequest().body("User is already a member of this forum");
        }
        forumMemberService.addMemberToForum(userId, forumId);
        return ResponseEntity.ok("User added to forum successfully");
    }

    @Operation(summary = "Leave a forum", description = "Allows a user to leave a forum they are currently a member of.")
    @DeleteMapping("/leave/{forumId}")
    public ResponseEntity<?> leaveForum(@AuthenticationPrincipal Jwt jwt, @PathVariable Long forumId) {
        var userId = jwt.getClaimAsString("sub");
        if (forumService.getForumById(forumId) == null) {
            return ResponseEntity.badRequest().body("Forum with that ID does not exist");
        }
        if (!forumMemberService.isMemberOfForum(userId, forumId)) {
            return ResponseEntity.badRequest().body("User is not a member of this forum");
        }
        forumMemberService.removeMemberFromForum(userId, forumId);
        return ResponseEntity.ok("User removed from forum successfully");
    }
}
