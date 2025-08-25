package com.tcode.moviebase.Services;


import com.tcode.moviebase.Dtos.ActorDto;
import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Entities.ActorGrade;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.ActorNotFoundException;
import com.tcode.moviebase.Exceptions.InvalidActorDataException;
import com.tcode.moviebase.Repositories.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;


    @Transactional
    public void deleteActor(Long id) {
        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
        } else {
            throw new ActorNotFoundException("Actor not found");
        }
    }



    public ActorDto saveActor(Actor actor) {
        if (actor.getFirstName() == null || actor.getLastName() == null || actor.getGender() == null) {
            throw new InvalidActorDataException("First name, last name, gender are required");
        }

        actorRepository.save(actor);
        return actorDtoToActorDto(actor);
    }

    public ActorDto updateActor(Long id, Actor actor) {
        var oldActor = actorRepository.findById(id).orElseThrow(() -> new ActorNotFoundException("Actor not found"));


        oldActor.setGender(actor.getGender());
        oldActor.setFirstName(actor.getFirstName());
        oldActor.setLastName(actor.getLastName());
        oldActor.setAge(actor.getAge());
        oldActor.setDateOfBirth(actor.getDateOfBirth());
        oldActor.setPlaceOfBirth(actor.getPlaceOfBirth());
        oldActor.setHeight(actor.getHeight());
        oldActor.setBiography(actor.getBiography());
        oldActor.setCountOfPrizes(actor.getCountOfPrizes());

        actorRepository.save(oldActor);

        return actorDtoToActorDto(oldActor);
    }

    private ActorDto actorDtoToActorDto(Actor actor) {
        ActorDto actorDto = new ActorDto();
        actorDto.setFirstName(actor.getFirstName());
        actorDto.setLastName(actor.getLastName());
        actorDto.setAge(actor.getAge());
        actorDto.setGender(actor.getGender());
        actorDto.setDateOfBirth(actor.getDateOfBirth());
        actorDto.setPlaceOfBirth(actor.getPlaceOfBirth());
        actorDto.setHeight(actor.getHeight());
        actorDto.setBiography(actor.getBiography());
        actorDto.setCountOfPrizes(actor.getCountOfPrizes());

        actorDto.setAvgGrade(actor.getActorGrades().stream()
                .mapToDouble(ActorGrade::getGrade)
                .average()
                .orElse(0.0)
        );

        actorDto.setMovieTitles(actor.getMovies().stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList())
        );

        return actorDto;
    }

    public ActorDto getActorDtoById(Long id) {
        var actor = actorRepository.findById(id).orElseThrow( () -> new ActorNotFoundException("Actor not found"));
        return actorDtoToActorDto(actor);
    }

    public Page<ActorDto> getActorsDto(Pageable pageable) {
        return actorRepository.findAll(pageable).map(this::actorDtoToActorDto);
    }



}
