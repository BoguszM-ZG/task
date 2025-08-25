package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.ForumThreadTitleDto;
import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Exceptions.ForumNotFoundException;
import com.tcode.moviebase.Repositories.ForumRepository;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForumThreadService {
    private final ForumThreadRepository forumThreadRepository;
    private final ForumRepository forumRepository;

    public ForumThreadTitleDto createThread(Long forumId, ForumThread forumThread) {
        var forum = forumRepository.findById(forumId).orElseThrow( () -> new ForumNotFoundException("Forum not found"));
        var thread = new ForumThread();
        thread.setThreadName(forumThread.getThreadName());
        thread.setForum(forum);

        forumThreadRepository.save(thread);

        return threadToTitleDto(thread);
    }

    public Page<ForumThreadTitleDto> getAllForumThreads(Pageable pageable) {
        return forumThreadRepository.findAll(pageable).map(this::threadToTitleDto);
    }

    public Page<ForumThreadTitleDto> getAllForumThreadsByForumId(Long forumId, Pageable pageable) {
        return forumThreadRepository.getAllForumThreadsByForumId(forumId, pageable).map(this::threadToTitleDto);
    }

    @Transactional
    public void deleteForumThread(Long forumThreadId) {
        if (!forumThreadRepository.existsById(forumThreadId)) {
            throw new ForumNotFoundException("Forum thread with that id does not exist");
        }
            forumThreadRepository.deleteById(forumThreadId);
    }

    public boolean existsById(Long forumThreadId) {
        return forumThreadRepository.existsById(forumThreadId);
    }

    public ForumThread findById(Long forumThreadId) {
        return forumThreadRepository.findById(forumThreadId).orElseThrow(() -> new ForumNotFoundException("Forum thread with that id does not exist"));
    }


    private ForumThreadTitleDto threadToTitleDto(ForumThread forumThread) {
        ForumThreadTitleDto dto = new ForumThreadTitleDto();
        dto.setThreadName(forumThread.getThreadName());
        return dto;
    }


}
