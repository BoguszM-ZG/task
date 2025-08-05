package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Repositories.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;


    public List<Actor> findAllActors() {
        return actorRepository.findAll();
    }

    @Transactional
    public void deleteActor(Long id) {
        actorRepository.deleteActorById(id);
    }

    public Actor getActorById(Long id) {
        return actorRepository.findById(id).orElse(null);
    }

    public Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }
}
