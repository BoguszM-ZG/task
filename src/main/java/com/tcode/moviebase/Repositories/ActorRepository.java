package com.tcode.moviebase.Repositories;

import com.tcode.moviebase.Entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    void deleteActorById(Long id);

    List<Actor> id(Long id);
}
