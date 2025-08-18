package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Repositories.ForumRepository;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForumThreadServiceTest {

    @Mock
    private ForumThreadRepository forumThreadRepository;

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private ForumThreadService forumThreadService;


    @Test
    public void testFindById() {
        ForumThread forumThread = new ForumThread();
        Long forumThreadId = 1L;
        when(forumThreadRepository.findById(forumThreadId)).thenReturn(java.util.Optional.of(forumThread));
        ForumThread result = forumThreadService.findById(forumThreadId);
        assertNotNull(result);
        assertEquals(forumThread, result);
    }

    @Test
    public void testFindAll() {
        var forumThreads = java.util.List.of(new ForumThread(), new ForumThread());
        when(forumThreadRepository.findAll()).thenReturn(forumThreads);
        var result = forumThreadService.getAllForumThreads();
        assertNotNull(result);
        assertEquals(forumThreads.size(), result.size());
    }

    @Test
    public void testGetAllForumThreadsByForumId() {
        Long forumId = 1L;
        var forumThreads = java.util.List.of(new ForumThread(), new ForumThread());
        when(forumThreadRepository.getAllForumThreadsByForumId(forumId)).thenReturn(forumThreads);
        var result = forumThreadService.getAllForumThreadsByForumId(forumId);
        assertNotNull(result);
        assertEquals(forumThreads.size(), result.size());
    }

    @Test
    public void testExistsById() {
        Long forumThreadId = 1L;
        when(forumThreadRepository.existsById(forumThreadId)).thenReturn(true);
        boolean exists = forumThreadService.existsById(forumThreadId);
        assertTrue(exists);
    }

    @Test
    public void testDeleteForumThread() {
        Long forumThreadId = 1L;
        forumThreadService.deleteForumThread(forumThreadId);

        assertFalse(forumThreadRepository.existsById(forumThreadId));
    }


    @Test
    public void testCreateThread() {
        Long forumId = 1L;
        Forum forum = new Forum();
        forum.setId(forumId);
        ForumThread forumThread = new ForumThread();
        forumThread.setThreadName("Test Thread");

        when(forumRepository.findById(forumId)).thenReturn(java.util.Optional.of(forum));
        when(forumThreadRepository.save(any(ForumThread.class))).thenReturn(forumThread);

        ForumThread createdThread = forumThreadService.createThread(forumId, forumThread);

        assertNotNull(createdThread);
        assertEquals("Test Thread", createdThread.getThreadName());
    }


}
