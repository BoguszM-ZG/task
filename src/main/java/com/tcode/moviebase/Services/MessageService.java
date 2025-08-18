package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Message;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import com.tcode.moviebase.Repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ForumThreadRepository forumThreadRepository;


    @Transactional
    public Message addMessage(String userId, Long threadId, String content) {
        var thread = forumThreadRepository.findById(threadId).orElse(null);
        Message message = new Message();
        message.setUserId(userId);
        message.setThread(thread);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByThreadId(Long threadId) {
        return messageRepository.findByThreadId(threadId);
    }

    public boolean existsById(Long id) {
        return messageRepository.existsById(id);
    }

    @Transactional
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
