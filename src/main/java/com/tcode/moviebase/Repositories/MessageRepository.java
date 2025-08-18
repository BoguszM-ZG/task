package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByThreadId(Long threadId);
}
