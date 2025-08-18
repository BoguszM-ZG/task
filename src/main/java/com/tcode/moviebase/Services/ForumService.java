package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Forum;
import com.tcode.moviebase.Repositories.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {
    private  final ForumRepository forumRepository;

    public Forum addForum(Forum forum) {
        return forumRepository.save(forum);
    }

    public Forum getForumById(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteForum(Long id) {
        forumRepository.deleteById(id);
    }

    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }



}
