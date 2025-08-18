package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.ForumThread;
import com.tcode.moviebase.Repositories.ForumRepository;
import com.tcode.moviebase.Repositories.ForumThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumThreadService {
    private final ForumThreadRepository forumThreadRepository;
    private final ForumRepository forumRepository;

    public ForumThread createThread(Long forumId, ForumThread forumThread) {
        var forum = forumRepository.findById(forumId).orElse(null);
        var thread = new ForumThread();
        thread.setThreadName(forumThread.getThreadName());
        thread.setForum(forum);

        return forumThreadRepository.save(thread);
    }

    public List<ForumThread> getAllForumThreads() {
        return forumThreadRepository.findAll();
    }

    public List<ForumThread> getAllForumThreadsByForumId(Long forumId) {
        return forumThreadRepository.getAllForumThreadsByForumId(forumId);
    }

    @Transactional
    public void deleteForumThread(Long forumThreadId) {
            forumThreadRepository.deleteById(forumThreadId);
    }

    public boolean existsById(Long forumThreadId) {
        return forumThreadRepository.existsById(forumThreadId);
    }

    public ForumThread findById(Long forumThreadId) {
        return forumThreadRepository.findById(forumThreadId).orElse(null);
    }


}
