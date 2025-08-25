package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.ForumThreadTitleDto;
import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Exceptions.ForumNotFoundException;
import com.tcode.moviebase.Repositories.ForumRepository;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumThreadServiceTest {

    @Mock
    private ForumThreadRepository forumThreadRepository;
    @Mock
    private ForumRepository forumRepository;

    @InjectMocks
    private ForumThreadService forumThreadService;

    @Test
    void createThread_ok() {
        Long forumId = 10L;
        Forum forum = new Forum();
        forum.setId(forumId);

        when(forumRepository.findById(forumId)).thenReturn(Optional.of(forum));
        when(forumThreadRepository.save(any(ForumThread.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ForumThread input = new ForumThread();
        input.setThreadName("Thread A");

        ForumThreadTitleDto dto = forumThreadService.createThread(forumId, input);

        assertNotNull(dto);
        assertEquals("Thread A", dto.getThreadName());
        verify(forumRepository).findById(forumId);
        verify(forumThreadRepository).save(any(ForumThread.class));
    }

    @Test
    void createThread_forumNotFound() {
        when(forumRepository.findById(anyLong())).thenReturn(Optional.empty());
        ForumThread input = new ForumThread();
        input.setThreadName("X");
        assertThrows(ForumNotFoundException.class,
                () -> forumThreadService.createThread(1L, input));
        verify(forumThreadRepository, never()).save(any());
    }

    @Test
    void getAllForumThreads_ok() {
        Pageable pageable = PageRequest.of(0, 5);
        ForumThread t1 = new ForumThread(); t1.setThreadName("A");
        ForumThread t2 = new ForumThread(); t2.setThreadName("B");
        Page<ForumThread> page = new PageImpl<>(List.of(t1, t2), pageable, 2);

        when(forumThreadRepository.findAll(pageable)).thenReturn(page);

        Page<ForumThreadTitleDto> result = forumThreadService.getAllForumThreads(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("A", result.getContent().get(0).getThreadName());
        assertEquals("B", result.getContent().get(1).getThreadName());
    }

    @Test
    void getAllForumThreadsByForumId_ok() {
        Long forumId = 7L;
        Pageable pageable = PageRequest.of(0, 10);
        ForumThread t1 = new ForumThread(); t1.setThreadName("One");
        Page<ForumThread> page = new PageImpl<>(List.of(t1), pageable, 1);

        when(forumThreadRepository.getAllForumThreadsByForumId(forumId, pageable))
                .thenReturn(page);

        Page<ForumThreadTitleDto> result =
                forumThreadService.getAllForumThreadsByForumId(forumId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("One", result.getContent().getFirst().getThreadName());
    }

    @Test
    void deleteForumThread_exists() {
        Long id = 3L;
        when(forumThreadRepository.existsById(id)).thenReturn(true);

        forumThreadService.deleteForumThread(id);

        verify(forumThreadRepository).deleteById(id);
    }

    @Test
    void deleteForumThread_notExists() {
        Long id = 4L;
        when(forumThreadRepository.existsById(id)).thenReturn(false);

        assertThrows(ForumNotFoundException.class,
                () -> forumThreadService.deleteForumThread(id));

        verify(forumThreadRepository, never()).deleteById(anyLong());
    }

    @Test
    void existsById_true() {
        when(forumThreadRepository.existsById(11L)).thenReturn(true);
        assertTrue(forumThreadService.existsById(11L));
    }

    @Test
    void findById_ok() {
        Long id = 9L;
        ForumThread thread = new ForumThread();
        when(forumThreadRepository.findById(id)).thenReturn(Optional.of(thread));

        ForumThread found = forumThreadService.findById(id);
        assertNotNull(found);
        assertEquals(thread, found);
    }

    @Test
    void findById_nullWhenMissing() {
        when(forumThreadRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(forumThreadService.findById(123L));
    }
}