package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.ForumMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumMemberRepository extends JpaRepository<ForumMember, Long> {

    ForumMember findByUserIdAndForumId(String userId, Long forumId);
}
