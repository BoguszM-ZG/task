package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.DirectorDto;
import com.tcode.moviebase.Entities.Director;
import com.tcode.moviebase.Entities.DirectorGrade;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.DirectorNotFoundException;
import com.tcode.moviebase.Exceptions.InvalidDirectorDataException;
import com.tcode.moviebase.Exceptions.MovieAlreadyAssignedToDirectorException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Repositories.DirectorRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectorServiceTest {

    @Mock
    private DirectorRepository directorRepository;
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private DirectorService directorService;

    @Test
    void addDirector_success() {
        Director d = baseDirector();
        when(directorRepository.save(d)).thenReturn(d);

        DirectorDto dto = directorService.addDirector(d);

        assertEquals(d.getFirstName(), dto.getFirstName());
        assertEquals(d.getLastName(), dto.getLastName());
        verify(directorRepository).save(d);
    }

    @Test
    void addDirector_missingFirstName_throws() {
        Director d = baseDirector();
        d.setFirstName("  ");
        assertThrows(InvalidDirectorDataException.class, () -> directorService.addDirector(d));
        verify(directorRepository, never()).save(any());
    }

    @Test
    void addDirector_missingLastName_throws() {
        Director d = baseDirector();
        d.setLastName(null);
        assertThrows(InvalidDirectorDataException.class, () -> directorService.addDirector(d));
    }

    @Test
    void addDirector_missingBirthDate_throws() {
        Director d = baseDirector();
        d.setDateOfBirth(null);
        assertThrows(InvalidDirectorDataException.class, () -> directorService.addDirector(d));
    }

    @Test
    void getDirectorById_success_mapsRatingAndMovies() {
        Director d = baseDirector();
        d.getDirectorGrades().add(grade(6));
        d.getDirectorGrades().add(grade(8));
        Movie m1 = movie("Film A");
        Movie m2 = movie("Film B");
        d.getMovies().add(m1);
        d.getMovies().add(m2);

        when(directorRepository.findById(1L)).thenReturn(Optional.of(d));

        DirectorDto dto = directorService.getDirectorById(1L);

        assertEquals(7.0, dto.getRating());
        assertEquals(List.of("Film A", "Film B"), dto.getMovieTitles());
    }

    @Test
    void getDirectorById_notFound_throws() {
        when(directorRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(DirectorNotFoundException.class, () -> directorService.getDirectorById(9L));
    }

    @Test
    void deleteDirector_exists_deletes() {
        when(directorRepository.existsById(5L)).thenReturn(true);
        directorService.deleteDirector(5L);
        verify(directorRepository).deleteById(5L);
    }

    @Test
    void deleteDirector_notExists_throws() {
        when(directorRepository.existsById(6L)).thenReturn(false);
        assertThrows(DirectorNotFoundException.class, () -> directorService.deleteDirector(6L));
    }

    @Test
    void addMovieToDirector_success() {
        Long directorId = 10L;
        Long movieId = 20L;

        Director d = baseDirector();
        d.setId(directorId);
        d.setMovies(new HashSet<>());
        Movie m = movie("Tytuł");
        m.setId(movieId);
        m.setDirectors(new HashSet<>());

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(d));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(m));

        directorService.addMovieToDirector(directorId, movieId);

        assertTrue(d.getMovies().contains(m));
        assertTrue(m.getDirectors().contains(d));
        verify(directorRepository).save(d);
        verify(movieRepository).save(m);
    }

    @Test
    void addMovieToDirector_directorNotFound_throws() {
        when(directorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DirectorNotFoundException.class, () -> directorService.addMovieToDirector(1L, 2L));
    }

    @Test
    void addMovieToDirector_movieNotFound_throws() {
        Director d = baseDirector();
        when(directorRepository.findById(1L)).thenReturn(Optional.of(d));
        when(movieRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> directorService.addMovieToDirector(1L, 2L));
    }

    @Test
    void addMovieToDirector_alreadyAssigned_throws() {
        Long directorId = 3L;
        Long movieId = 4L;
        Director d = baseDirector();
        d.setId(directorId);
        Movie m = movie("X");
        m.setId(movieId);

        d.getMovies().add(m);
        m.setDirectors(new HashSet<>());
        m.getDirectors().add(d);

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(d));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(m));

        assertThrows(MovieAlreadyAssignedToDirectorException.class,
                () -> directorService.addMovieToDirector(directorId, movieId));
        verify(directorRepository, never()).save(any());
    }

    @Test
    void getDirectorsDtoByGender_maps() {
        Pageable p = PageRequest.of(0, 10);
        Director d = baseDirector();
        d.getDirectorGrades().add(grade(10));
        Page<Director> page = new PageImpl<>(List.of(d), p, 1);

        when(directorRepository.getDirectorsByGender("M", p)).thenReturn(page);

        Page<DirectorDto> result = directorService.getDirectorsDtoByGender("M", p);

        assertEquals(1, result.getTotalElements());
        assertEquals(10.0, result.getContent().getFirst().getRating());
    }

    @Test
    void getDirectorsDtoSortedByLastNameAsc_maps() {
        Pageable p = PageRequest.of(0, 5);
        Director d = baseDirector();
        Page<Director> page = new PageImpl<>(List.of(d), p, 1);
        when(directorRepository.getDirectorsOrderByLastNameAsc(p)).thenReturn(page);

        Page<DirectorDto> result = directorService.getDirectorsDtoSortedByLastNameAsc(p);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getDirectorsDtoSortedByLastNameDesc_maps() {
        Pageable p = PageRequest.of(0, 5);
        Director d = baseDirector();
        Page<Director> page = new PageImpl<>(List.of(d), p, 1);
        when(directorRepository.getDirectorsOrderByLastNameDesc(p)).thenReturn(page);

        Page<DirectorDto> result = directorService.getDirectorsDtoSortedByLastNameDesc(p);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getDirectorsByFirstName_maps() {
        Pageable p = PageRequest.of(0, 5);
        Director d = baseDirector();
        Page<Director> page = new PageImpl<>(List.of(d), p, 1);
        when(directorRepository.getDirectorByFirstName("Jan", p)).thenReturn(page);

        Page<DirectorDto> result = directorService.getDirectorsByFirstName("Jan", p);

        assertEquals("Jan", result.getContent().getFirst().getFirstName());
    }

    @Test
    void getDirectorsByLastName_maps() {
        Pageable p = PageRequest.of(0, 5);
        Director d = baseDirector();
        Page<Director> page = new PageImpl<>(List.of(d), p, 1);
        when(directorRepository.getDirectorByLastName("Kowalski", p)).thenReturn(page);

        Page<DirectorDto> result = directorService.getDirectorsByLastName("Kowalski", p);

        assertEquals("Kowalski", result.getContent().getFirst().getLastName());
    }

    @Test
    void getAllDirectors_maps() {
        Pageable p = PageRequest.of(0, 5);
        Director d = baseDirector();
        d.getDirectorGrades().add(grade(5));
        d.getDirectorGrades().add(grade(7));
        Page<Director> page = new PageImpl<>(List.of(d), p, 1);
        when(directorRepository.findAll(p)).thenReturn(page);

        Page<DirectorDto> result = directorService.getAllDirectors(p);

        assertEquals(1, result.getTotalElements());
        assertEquals(6.0, result.getContent().getFirst().getRating());
    }

    private Director baseDirector() {
        Director d = new Director();
        d.setFirstName("Jan");
        d.setLastName("Kowalski");
        d.setDateOfBirth(LocalDate.of(1970, 1, 1));
        d.setGender("M");
        d.setBiography("Bio");
        if (d.getDirectorGrades() == null) d.setDirectorGrades(new ArrayList<>());
        if (d.getMovies() == null) d.setMovies(new HashSet<>());
        return d;
    }

    private DirectorGrade grade(int g) {
        DirectorGrade dg = new DirectorGrade();
        dg.setGrade(g);
        return dg;
    }

    private Movie movie(String title) {
        Movie m = new Movie();
        m.setTitle(title);
        m.setDirectors(new HashSet<>());
        return m;
    }
}