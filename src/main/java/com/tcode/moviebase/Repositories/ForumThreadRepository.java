package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {

    List<ForumThread> getAllForumThreadsByForumId(Long forumId);
}
