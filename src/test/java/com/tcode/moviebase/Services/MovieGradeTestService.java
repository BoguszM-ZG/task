package com.tcode.moviebase.Services;


import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import com.tcode.moviebase.Repositories.MovieGradeRepository;
import com.tcode.moviebase.Repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieGradeTestService {

    @Mock
    private MovieGradeRepository movieGradeRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieGradeService movieGradeService;

    @Test
    void testGetAvgGrade(){
        Long movieId = 1L;
        MovieGrade grade1 = new MovieGrade();
        grade1.setUserId("user1");
        grade1.setGrade(4);
        MovieGrade grade2 = new MovieGrade();
        grade2.setGrade(6);
        grade2.setUserId("user2");
        when(movieGradeRepository.findByMovieId(movieId)).thenReturn(Arrays.asList(grade1, grade2));

        Double result = movieGradeService.getAvgGrade(movieId);

        assertEquals(5.0, result);
        verify(movieGradeRepository).findByMovieId(movieId);
    }

    @Test
    void testGetAvgGradeWithNoGrades() {
        Long movieId = 2L;
        when(movieGradeRepository.findByMovieId(movieId)).thenReturn(List.of());

        Double result = movieGradeService.getAvgGrade(movieId);

        assertEquals(0.0, result);
        verify(movieGradeRepository).findByMovieId(movieId);
    }

    @Test
    void getAvgGradeReturnsNullWhenNoGrades() {
        Long movieId = 1L;
        when(movieGradeRepository.findByMovieId(movieId)).thenReturn(List.of());

        Double result = movieGradeService.getAvgGrade(movieId);

        assertEquals(0.0, result);
        verify(movieGradeRepository).findByMovieId(movieId);
    }

    @Test
    void addGradeReturnsNullWhenMovieNotFound() {
        Long movieId = 1L;
        int grade = 5;
        var userId = "user1";
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        MovieGrade result = movieGradeService.addGrade(userId, movieId, grade);

        assertNull(result);
    }

    @Test
    void addGradeTest() {
        Long movieId = 1L;
        int grade = 5;
        var userId = "user1";
        Movie movie = new Movie();
        MovieGrade movieGrade = new MovieGrade();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieGradeRepository.save(any(MovieGrade.class))).thenReturn(movieGrade);

        MovieGrade result = movieGradeService.addGrade(userId, movieId, grade);

        assertEquals(movieGrade, result);
        verify(movieRepository).findById(movieId);
        verify(movieGradeRepository).save(any(MovieGrade.class));
    }

    @Test
    void getAvgGradeByUserIdTest() {
        String userId = "user1";
        MovieGrade grade1 = new MovieGrade();
        grade1.setUserId(userId);
        grade1.setGrade(4);
        MovieGrade grade2 = new MovieGrade();
        grade2.setUserId(userId);
        grade2.setGrade(6);
        when(movieGradeRepository.findByUserId(userId)).thenReturn(Arrays.asList(grade1, grade2));

        Double result = movieGradeService.getAvgGradeByUserId(userId);

        assertEquals(5.0, result);
        verify(movieGradeRepository).findByUserId(userId);
    }

    @Test
    void getAvgGradeByUserIdReturnsZeroWhenNoGrades() {
        String userId = "user2";
        when(movieGradeRepository.findByUserId(userId)).thenReturn(List.of());
        Double result = movieGradeService.getAvgGradeByUserId(userId);
        assertEquals(0.0, result);
        verify(movieGradeRepository).findByUserId(userId);
    }

    @Test
    void getAvgGradeGivenYearAndMonthTest() {
        String userId = "user3";
        int year = 2023;
        int month = 10;
        MovieGrade grade1 = new MovieGrade();
        grade1.setUserId(userId);
        grade1.setGrade(4);
        MovieGrade grade2 = new MovieGrade();
        grade2.setUserId(userId);
        grade2.setGrade(6);
        when(movieGradeRepository.findByUserIdInYearAndMonth(userId, year, month)).thenReturn(Arrays.asList(grade1, grade2));
        Double result = movieGradeService.getAvgGradeGivenYearAndMonth(userId, year, month);
        assertEquals(5.0, result);
        verify(movieGradeRepository).findByUserIdInYearAndMonth(userId, year, month);
    }
}
