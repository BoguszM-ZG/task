package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.ActorGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorGradeRepository extends JpaRepository<ActorGrade,Long> {
    List<ActorGrade> findByActorId(Long actorId);
}
