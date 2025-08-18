package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Entities.ForumMember;
import com.tcode.moviebase.Repositories.ForumMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForumMemberServiceTest {

    @Mock
    private ForumMemberRepository forumMemberRepository;

    @InjectMocks
    private ForumMemberService forumMemberService;

    @Mock
    private ForumService forumService;


    @Test
    void testIsMemberOfForum_true() {
        Forum forum = new Forum();
        forum.setId(1L);
        ForumMember member = new ForumMember();
        member.setUserId("user1");
        member.setForum(forum);

        when(forumMemberRepository.findAll()).thenReturn(List.of(member));

        assertTrue(forumMemberService.isMemberOfForum("user1", 1L));
    }

    @Test
    void testIsMemberOfForum_false() {
        when(forumMemberRepository.findAll()).thenReturn(List.of());
        assertFalse(forumMemberService.isMemberOfForum("user1", 1L));
    }


    @Test
    void testAddMemberToForum() {
        Forum forum = new Forum();
        forum.setId(1L);

        when(forumService.getForumById(1L)).thenReturn(forum);

        forumMemberService.addMemberToForum("user1", 1L);

        verify(forumMemberRepository).save(any(ForumMember.class));
    }

    @Test
    void testRemoveMemberFromForum() {
        ForumMember member = new ForumMember();
        member.setUserId("user1");
        Forum forum = new Forum();
        forum.setId(1L);
        member.setForum(forum);

        when(forumMemberRepository.findByUserIdAndForumId("user1", 1L)).thenReturn(member);

        forumMemberService.removeMemberFromForum("user1", 1L);

        verify(forumMemberRepository).delete(member);
    }
}
