package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MessageDto;
import com.tcode.moviebase.Entities.Message;
import com.tcode.moviebase.Exceptions.MessageDoesntExistsException;
import com.tcode.moviebase.Exceptions.ThreadDoesntExistsException;
import com.tcode.moviebase.Exceptions.UserIsNotMemberOfForumException;
import com.tcode.moviebase.Repositories.ForumMemberRepository;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import com.tcode.moviebase.Repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ForumThreadRepository forumThreadRepository;
    private final ForumService forumService;
    private final ForumMemberRepository forumMemberRepository;


    @Transactional
    public MessageDto addMessage(String userId, Long threadId, String content, Long forumId) {
        var thread = forumThreadRepository.findById(threadId).orElseThrow(() -> new ThreadDoesntExistsException("Thread doesnt exist"));
        if (forumMemberRepository.findByUserIdAndForumId(userId, forumId) == null) {
            throw new UserIsNotMemberOfForumException("User is not a member of the forum");
        }
        Message message = new Message();
        message.setUserId(userId);
        message.setThread(thread);
        message.setContent(content);
        messageRepository.save(message);
        return messageToMessageDto(message);
    }

    public Page<MessageDto> getMessagesByThreadId(Long threadId, Pageable pageable) {
        if (!forumThreadRepository.existsById(threadId)) {
            throw new ThreadDoesntExistsException("Thread doesnt exist");
        }
        return messageRepository.findByThreadId(threadId, pageable)
                .map(this::messageToMessageDto);
    }

    public boolean existsById(Long id) {
        return messageRepository.existsById(id);
    }

    @Transactional
    public void deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
        }
        else {
            throw new MessageDoesntExistsException("Message doesnt exist");
        }

    }

    private MessageDto messageToMessageDto(Message message) {
        var dto = new MessageDto();
        dto.setUserId(message.getUserId());
        dto.setContent(message.getContent());
        return dto;
    }
}
