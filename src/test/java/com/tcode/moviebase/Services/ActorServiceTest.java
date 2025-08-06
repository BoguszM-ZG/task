package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Repositories.ActorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    @Test
    void testGetAllActors(){
        var a1 = new Actor();
        var a2 = new Actor();

        when(actorRepository.findAll()).thenReturn(Arrays.asList(a1,a2));

        var result = actorService.findAllActors();

        assertEquals(2, result.size());
    }

    @Test
    void testGetActorById(){
        var a1 = new Actor();
        a1.setId(1L);

        when(actorRepository.findById(1L)).thenReturn(Optional.of(a1));

        var result = actorService.getActorById(a1.getId());

        assertEquals(1, result.getId());
    }

    @Test
    void testDeleteActor(){
        Long id = 1L;

        actorService.deleteActor(id);
        verify(actorRepository).deleteActorById(id);
    }

    @Test
    void testAddActor() {
        var actor = new Actor();
        when(actorRepository.save(actor)).thenReturn(actor);

        var result = actorService.saveActor(actor);

        assertNotNull(result);
        verify(actorRepository).save(actor);
    }

    @Test
    void testUpdateActor() {
        var oldActor = new Actor();
        oldActor.setId(1L);
        oldActor.setFirstName("Old");
        oldActor.setLastName("Actor");

        var newActor = new Actor();
        newActor.setFirstName("New");
        newActor.setLastName("Actor");

        when(actorRepository.findById(1L)).thenReturn(Optional.of(oldActor));
        when(actorRepository.save(oldActor)).thenReturn(oldActor);

        var result = actorService.updateActor(1L, newActor);

        assertNotNull(result);
        assertEquals("New", result.getFirstName());
        assertEquals("Actor", result.getLastName());
    }
}
