package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Repositories.ForumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForumServiceTest {

    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private ForumService forumService;


    @Test
    public void testAddForum() {
        var forum = new Forum();
        when(forumRepository.save(forum)).thenReturn(forum);

        Forum result = forumService.addForum(forum);
        assertNotNull(result);
        verify(forumRepository).save(forum);
    }

    @Test
    public void testGetForumById() {
        Long forumId = 1L;
        var forum = new Forum();
        when(forumRepository.findById(forumId)).thenReturn(java.util.Optional.of(forum));

        Forum result = forumService.getForumById(forumId);
        assertNotNull(result);
        verify(forumRepository).findById(forumId);
    }

    @Test
    public void testDeleteForum() {
        Long forumId = 1L;

        forumService.deleteForum(forumId);
        verify(forumRepository).deleteById(forumId);
    }

    @Test
    public void testGetAllForums() {
        var forums = java.util.List.of(new Forum(), new Forum());
        when(forumRepository.findAll()).thenReturn(forums);
        var result = forumService.getAllForums();
        assertNotNull(result);
        verify(forumRepository).findAll();
    }
}
