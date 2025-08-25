package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.ActorDto;
import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Entities.ActorGrade;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.ActorNotFoundException;
import com.tcode.moviebase.Exceptions.InvalidActorDataException;
import com.tcode.moviebase.Repositories.ActorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    @Test
    void saveActor_success() {
        Actor actor = baseActor();
        when(actorRepository.save(actor)).thenReturn(actor);

        ActorDto dto = actorService.saveActor(actor);

        assertNotNull(dto);
        assertEquals("John", dto.getFirstName());
        verify(actorRepository).save(actor);
    }

    @Test
    void saveActor_missingFirstName_throws() {
        Actor actor = baseActor();
        actor.setFirstName(null);
        assertThrows(InvalidActorDataException.class, () -> actorService.saveActor(actor));
        verify(actorRepository, never()).save(any());
    }

    @Test
    void saveActor_missingLastName_throws() {
        Actor actor = baseActor();
        actor.setLastName(null);
        assertThrows(InvalidActorDataException.class, () -> actorService.saveActor(actor));
    }

    @Test
    void saveActor_missingGender_throws() {
        Actor actor = baseActor();
        actor.setGender(null);
        assertThrows(InvalidActorDataException.class, () -> actorService.saveActor(actor));
    }

    @Test
    void deleteActor_existing_deletes() {
        when(actorRepository.existsById(1L)).thenReturn(true);

        actorService.deleteActor(1L);

        verify(actorRepository).deleteById(1L);
    }

    @Test
    void deleteActor_notExisting_throws() {
        when(actorRepository.existsById(2L)).thenReturn(false);
        assertThrows(ActorNotFoundException.class, () -> actorService.deleteActor(2L));
        verify(actorRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateActor_success() {
        Actor existing = baseActor();
        existing.setId(3L);
        Actor update = baseActor();
        update.setFirstName("Jane");
        update.setLastName("Smith");
        update.setBiography("New bio");
        update.setAge(40);

        when(actorRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(actorRepository.save(existing)).thenReturn(existing);

        ActorDto dto = actorService.updateActor(3L, update);

        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("New bio", dto.getBiography());
        assertEquals(40, dto.getAge());
        verify(actorRepository).save(existing);
    }

    @Test
    void updateActor_notFound_throws() {
        when(actorRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ActorNotFoundException.class, () -> actorService.updateActor(9L, baseActor()));
    }

    @Test
    void getActorDtoById_mapsAvgGradeAndMovies() {
        Actor actor = baseActor();
        actor.setId(7L);
        actor.setActorGrades(new ArrayList<>());
        actor.setMovies(new ArrayList<>());

        ActorGrade g1 = new ActorGrade();
        g1.setGrade(4);
        ActorGrade g2 = new ActorGrade();
        g2.setGrade(6);
        actor.getActorGrades().add(g1);
        actor.getActorGrades().add(g2);

        Movie m1 = new Movie(); m1.setTitle("Movie A");
        Movie m2 = new Movie(); m2.setTitle("Movie B");
        actor.getMovies().add(m1);
        actor.getMovies().add(m2);

        when(actorRepository.findById(7L)).thenReturn(Optional.of(actor));

        ActorDto dto = actorService.getActorDtoById(7L);

        assertEquals(5.0, dto.getAvgGrade());
        assertEquals(2, dto.getMovieTitles().size());
        assertTrue(dto.getMovieTitles().contains("Movie A"));
    }

    @Test
    void getActorDtoById_notFound_throws() {
        when(actorRepository.findById(15L)).thenReturn(Optional.empty());
        assertThrows(ActorNotFoundException.class, () -> actorService.getActorDtoById(15L));
    }

    @Test
    void getActorsDto_paginationAndMapping() {
        Actor a1 = baseActor();
        a1.setActorGrades(new ArrayList<>());
        a1.setMovies(new ArrayList<>());
        ActorGrade g = new ActorGrade(); g.setGrade(8);
        a1.getActorGrades().add(g);
        Movie mov = new Movie(); mov.setTitle("X");
        a1.getMovies().add(mov);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Actor> page = new PageImpl<>(List.of(a1), pageable, 1);
        when(actorRepository.findAll(pageable)).thenReturn(page);

        Page<ActorDto> dtoPage = actorService.getActorsDto(pageable);

        assertEquals(1, dtoPage.getTotalElements());
        assertEquals(8.0, dtoPage.getContent().getFirst().getAvgGrade());
        assertEquals(List.of("X"), dtoPage.getContent().getFirst().getMovieTitles());
    }

    private Actor baseActor() {
        Actor a = new Actor();
        a.setFirstName("John");
        a.setLastName("Doe");
        a.setGender("M");
        a.setAge(30);
        a.setBiography("Bio");
        a.setDateOfBirth(LocalDate.of(1994,1,1));
        a.setPlaceOfBirth("City");
        a.setHeight(180);
        a.setCountOfPrizes(2);
        if (a.getActorGrades() == null) a.setActorGrades(new ArrayList<>());
        if (a.getMovies() == null) a.setMovies(new ArrayList<>());
        return a;
    }
}