package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Actor;
import com.tcode.moviebase.Entities.ActorGrade;
import com.tcode.moviebase.Repositories.ActorGradeRepository;
import com.tcode.moviebase.Repositories.ActorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActorGradeServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private ActorGradeRepository actorGradeRepository;

    @InjectMocks
    private ActorGradeService actorGradeService;

    @Test
    void testAddActorGradeAndGetAvg() {

        Long actorId = 1L;
        var actorGrade1 = new ActorGrade();
        actorGrade1.setGrade(4);

        var actorGrade2 = new ActorGrade();
        actorGrade2.setGrade(5);

        when(actorGradeRepository.findByActorId(actorId)).thenReturn(Arrays.asList(actorGrade1, actorGrade2));

        var result = actorGradeService.getAvgGrade(actorId);

        assertEquals(4.5, result);
    }

    @Test
    void testGetAvgGradeWithNoGrades() {
        Long actorId = 2L;
        when(actorGradeRepository.findByActorId(actorId)).thenReturn(Arrays.asList());

        var result = actorGradeService.getAvgGrade(actorId);

        assertNull(result);
    }

    @Test
    void testAddActorGrade() {
        Long actorId = 1L;
        int grade = 5;

        var actor = new Actor();
        var actorGrade = new ActorGrade();
        when(actorRepository.findById(actorId)).thenReturn(Optional.of(actor));
        when(actorGradeRepository.save(any(ActorGrade.class))).thenReturn(actorGrade);

        var result = actorGradeService.addActorGrade(actorId, grade);

        assertEquals(actorGrade, result);
        verify(actorRepository).findById(actorId);
        verify(actorGradeRepository).save(any(ActorGrade.class));

    }

    @Test
    void testAddActorGradeReturnsNullWhenActorNotFound() {
        Long actorId = 1L;
        int grade = 5;
        when(actorRepository.findById(actorId)).thenReturn(Optional.empty());

        var result = actorGradeService.addActorGrade(actorId, grade);
        assertNull(result);
    }

}
