package com.tcode.moviebase.Services;

import com.tcode.moviebase.Entities.Director;
import com.tcode.moviebase.Entities.DirectorGrade;
import com.tcode.moviebase.Exceptions.DirectorNotFoundException;
import com.tcode.moviebase.Exceptions.GradeOutOfRangeException;
import com.tcode.moviebase.Repositories.DirectorGradeRepository;
import com.tcode.moviebase.Repositories.DirectorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectorGradeServiceTest {

    @Mock
    private DirectorGradeRepository directorGradeRepository;
    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorGradeService directorGradeService;

    private Director director() {
        Director d = new Director();
        d.setId(100L);
        return d;
    }

    @Test
    void addDirectorGrade_success() {
        Director d = director();
        when(directorRepository.findById(100L)).thenReturn(Optional.of(d));
        when(directorGradeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        directorGradeService.addDirectorGrade("user1", 100L, 7);

        ArgumentCaptor<DirectorGrade> captor = ArgumentCaptor.forClass(DirectorGrade.class);
        verify(directorGradeRepository).save(captor.capture());
        DirectorGrade saved = captor.getValue();
        assertEquals("user1", saved.getUserId());
        assertEquals(7, saved.getGrade());
        assertEquals(d, saved.getDirector());
    }

    @Test
    void addDirectorGrade_boundaryMin_0() {
        Director d = director();
        when(directorRepository.findById(100L)).thenReturn(Optional.of(d));
        directorGradeService.addDirectorGrade("u", 100L, 0);
        verify(directorGradeRepository).save(any(DirectorGrade.class));
    }

    @Test
    void addDirectorGrade_boundaryMax_10() {
        Director d = director();
        when(directorRepository.findById(100L)).thenReturn(Optional.of(d));
        directorGradeService.addDirectorGrade("u", 100L, 10);
        verify(directorGradeRepository).save(any(DirectorGrade.class));
    }

    @Test
    void addDirectorGrade_directorNotFound_throws() {
        when(directorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DirectorNotFoundException.class,
                () -> directorGradeService.addDirectorGrade("u", 1L, 5));
        verify(directorGradeRepository, never()).save(any());
    }

    @Test
    void addDirectorGrade_gradeTooLow_throws() {
        when(directorRepository.findById(1L)).thenReturn(Optional.of(director()));
        assertThrows(GradeOutOfRangeException.class,
                () -> directorGradeService.addDirectorGrade("u", 1L, -1));
        verify(directorGradeRepository, never()).save(any());
    }

    @Test
    void addDirectorGrade_gradeTooHigh_throws() {
        when(directorRepository.findById(1L)).thenReturn(Optional.of(director()));
        assertThrows(GradeOutOfRangeException.class,
                () -> directorGradeService.addDirectorGrade("u", 1L, 11));
        verify(directorGradeRepository, never()).save(any());
    }

    @Test
    void updateDirectorGrade_existing_replaces() {
        String user = "userX";
        Long dirId = 100L;
        Director d = director();
        DirectorGrade existing = new DirectorGrade();
        existing.setUserId(user);
        existing.setDirector(d);
        existing.setGrade(4);

        when(directorGradeRepository.findByUserIdAndDirectorId(user, dirId)).thenReturn(existing);
        when(directorRepository.findById(dirId)).thenReturn(Optional.of(d));
        when(directorGradeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        directorGradeService.updateDirectorGrade(user, dirId, 9);

        verify(directorGradeRepository).delete(existing);
        ArgumentCaptor<DirectorGrade> captor = ArgumentCaptor.forClass(DirectorGrade.class);
        verify(directorGradeRepository).save(captor.capture());
        assertEquals(9, captor.getValue().getGrade());
    }

    @Test
    void updateDirectorGrade_noExisting_addsOnly() {
        String user = "u2";
        Long dirId = 200L;
        Director d = director();
        d.setId(dirId);

        when(directorGradeRepository.findByUserIdAndDirectorId(user, dirId)).thenReturn(null);
        when(directorRepository.findById(dirId)).thenReturn(Optional.of(d));

        directorGradeService.updateDirectorGrade(user, dirId, 6);

        verify(directorGradeRepository, never()).delete(any());
        verify(directorGradeRepository).save(any());
    }

    @Test
    void existsDirectorGrade_true() {
        when(directorGradeRepository.existsByUserIdAndDirectorId("u", 5L)).thenReturn(true);
        assertTrue(directorGradeService.existsDirectorGrade("u", 5L));
    }

    @Test
    void existsDirectorGrade_false() {
        when(directorGradeRepository.existsByUserIdAndDirectorId("u", 6L)).thenReturn(false);
        assertFalse(directorGradeService.existsDirectorGrade("u", 6L));
    }

    @Test
    void getAvgDirectorGrade_withGrades() {
        Long dirId = 300L;
        DirectorGrade g1 = new DirectorGrade(); g1.setGrade(6);
        DirectorGrade g2 = new DirectorGrade(); g2.setGrade(8);
        when(directorGradeRepository.findByDirectorId(dirId)).thenReturn(List.of(g1, g2));

        Double avg = directorGradeService.getAvgDirectorGrade(dirId);

        assertEquals(7.0, avg);
    }

    @Test
    void getAvgDirectorGrade_empty() {
        when(directorGradeRepository.findByDirectorId(400L)).thenReturn(List.of());
        assertEquals(0.0, directorGradeService.getAvgDirectorGrade(400L));
    }
}