package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.ForumMember;
import com.tcode.moviebase.Repositories.ForumMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForumMemberService {

    private final ForumMemberRepository forumMemberRepository;
    private final ForumService forumService;

    public boolean isMemberOfForum(String userId, Long forumId) {
        return forumMemberRepository.findAll().stream()
                .anyMatch(member -> member.getUserId().equals(userId) && member.getForum().getId().equals(forumId));
    }

    public void addMemberToForum(String userId, Long forumId){
        var forumMember = new ForumMember();
        forumMember.setUserId(userId);
        forumMember.setForum(forumService.getForumById(forumId));
        forumMemberRepository.save(forumMember);
    }

    @Transactional
    public void removeMemberFromForum(String userId, Long forumId){
        var user = forumMemberRepository.findByUserIdAndForumId(userId, forumId);
        forumMemberRepository.delete(user);
    }
}
