package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Message;
import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import com.tcode.moviebase.Repositories.MessageRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ForumThreadRepository forumThreadRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void testAddMessage() {
        String userId = "user1";
        Long threadId = 1L;
        String content = "Test message";
        ForumThread thread = new ForumThread();
        thread.setId(threadId);

        when(forumThreadRepository.findById(threadId)).thenReturn(Optional.of(thread));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Message result = messageService.addMessage(userId, threadId, content);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(thread, result.getThread());
        assertEquals(content, result.getContent());
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void testGetMessagesByThreadId() {
        Long threadId = 1L;
        Message msg1 = new Message();
        Message msg2 = new Message();
        when(messageRepository.findByThreadId(threadId)).thenReturn(List.of(msg1, msg2));

        List<Message> result = messageService.getMessagesByThreadId(threadId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(messageRepository).findByThreadId(threadId);
    }

    @Test
    void testExistsById() {
        Long id = 1L;
        when(messageRepository.existsById(id)).thenReturn(true);

        boolean exists = messageService.existsById(id);

        assertTrue(exists);
        verify(messageRepository).existsById(id);
    }

    @Test
    void testDeleteMessage() {
        Long id = 1L;

        doNothing().when(messageRepository).deleteById(id);

        messageService.deleteMessage(id);

        verify(messageRepository).deleteById(id);
    }
}