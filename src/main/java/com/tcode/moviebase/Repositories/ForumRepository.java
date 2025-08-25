package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum,Long> {
}
