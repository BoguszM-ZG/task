package com.tcode.moviebase.Services;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Exceptions.InvalidMovieDataException;
import com.tcode.moviebase.Exceptions.MovieNotFoundException;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private WatchedMoviesRepository watchedMoviesRepository;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    @Test
    void addMovie_success() {
        Movie movie = new Movie();
        movie.setTitle("Title");
        movie.setMovie_year(2024);

        MovieWithAvgGradeDto dto = dummyDto("Title");
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        MovieWithAvgGradeDto result = movieService.addMovie(movie);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        verify(movieRepository).save(movie);
        verify(movieMapper).movieToMovieWithAvgGradeDto(movie);
    }

    @Test
    void addMovie_missingTitle_throws() {
        Movie movie = new Movie();
        movie.setMovie_year(2024);
        assertThrows(InvalidMovieDataException.class, () -> movieService.addMovie(movie));
        verify(movieRepository, never()).save(any());
    }

    @Test
    void addMovie_missingYear_throws() {
        Movie movie = new Movie();
        movie.setTitle("X");
        assertThrows(InvalidMovieDataException.class, () -> movieService.addMovie(movie));
        verify(movieRepository, never()).save(any());
    }

    @Test
    void deleteMovie_existing_deletes() {
        Long id = 10L;
        when(movieRepository.existsById(id)).thenReturn(true);

        movieService.deleteMovie(id);

        verify(movieRepository).deleteById(id);
    }

    @Test
    void deleteMovie_notExisting_throws() {
        Long id = 11L;
        when(movieRepository.existsById(id)).thenReturn(false);
        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(id));
        verify(movieRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateMovie_updatesSelectedFields() {
        Long id = 1L;
        Movie existing = new Movie();
        existing.setId(id);
        existing.setCategory("Old");
        existing.setDescription("OldDesc");
        existing.setPrizes("OldPrizes");
        existing.setTag("OldTag");
        existing.setWorld_premiere(LocalDate.of(2020,1,1));
        existing.setPolish_premiere(LocalDate.of(2020,2,2));

        Movie update = new Movie();
        update.setCategory("NewCat");
        update.setDescription("NewDesc");
        update.setPrizes("NewPrizes");
        update.setTag("NewTag");
        update.setWorld_premiere(LocalDate.of(2024,3,3));
        update.setPolish_premiere(LocalDate.of(2024,4,4));

        MovieWithAvgGradeDto mapped = dummyDto("Whatever");
        when(movieRepository.findById(id)).thenReturn(Optional.of(existing));
        when(movieRepository.save(existing)).thenReturn(existing);
        when(movieMapper.movieToMovieWithAvgGradeDto(existing)).thenReturn(mapped);

        MovieWithAvgGradeDto result = movieService.updateMovie(id, update);

        assertNotNull(result);
        assertEquals("NewCat", existing.getCategory());
        assertEquals("NewDesc", existing.getDescription());
        assertEquals("NewPrizes", existing.getPrizes());
        assertEquals("NewTag", existing.getTag());
        assertEquals(LocalDate.of(2024,3,3), existing.getWorld_premiere());
        assertEquals(LocalDate.of(2024,4,4), existing.getPolish_premiere());
        verify(movieRepository).save(existing);
        verify(movieMapper).movieToMovieWithAvgGradeDto(existing);
    }

    @Test
    void updateMovie_notFound_throws() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(99L, new Movie()));
    }

    @Test
    void getMovieById_found() {
        Long id = 5L;
        Movie movie = new Movie();
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(id);

        assertSame(movie, result);
        verify(movieRepository).findById(id);
    }

    @Test
    void getMovieById_notFound_throws() {
        when(movieRepository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieById(6L));
    }

    @Test
    void getMoviesWithAvgGradeDesc_delegates() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MovieWithAvgGradeDto> page = new PageImpl<>(List.of(dummyDto("A")));
        when(movieRepository.findAllMoviesWithAvgGradeDesc(pageable)).thenReturn(page);

        Page<MovieWithAvgGradeDto> result = movieService.getMoviesWithAvgGradeDesc(pageable);

        assertEquals(1, result.getTotalElements());
        verify(movieRepository).findAllMoviesWithAvgGradeDesc(pageable);
    }

    @Test
    void getMoviesWithAvgGradeAsc_delegates() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<MovieWithAvgGradeDto> page = new PageImpl<>(List.of(dummyDto("B")));
        when(movieRepository.findAllMoviesWithAvgGradeAsc(pageable)).thenReturn(page);

        Page<MovieWithAvgGradeDto> result = movieService.getMoviesWithAvgGradeAsc(pageable);

        assertEquals(1, result.getTotalElements());
        verify(movieRepository).findAllMoviesWithAvgGradeAsc(pageable);
    }

    @Test
    void getTopTenMoviesWithAvgGrade_delegates() {
        var list = List.of(dummyDto("M1"), dummyDto("M2"));
        when(movieRepository.findTop10MoviesByAvgGrade()).thenReturn(list);

        List<MovieWithAvgGradeDto> result = movieService.getTopTenMoviesWithAvgGrade();

        assertEquals(2, result.size());
        verify(movieRepository).findTop10MoviesByAvgGrade();
    }

    @Test
    void getMoviesPropositionForUser() {
        String userId = "u1";
        Movie m1 = new Movie(); m1.setCategory("Action");
        Movie m2 = new Movie(); m2.setCategory("Drama");
        List<Movie> watched = List.of(m1, m2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<MovieWithAvgGradeDto> propositions = new PageImpl<>(List.of(dummyDto("X"), dummyDto("Y")));
        when(watchedMoviesRepository.findMoviesByUserId(userId)).thenReturn(watched);
        when(movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies(
                List.of("Action","Drama"), watched, pageable)).thenReturn(propositions);

        Page<MovieWithAvgGradeDto> result = movieService.getMoviesPropositionForUser(userId, pageable);

        assertEquals(2, result.getTotalElements());
        verify(watchedMoviesRepository).findMoviesByUserId(userId);
    }

    @Test
    void movieExists_true() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        assertTrue(movieService.movieExists(1L));
    }

    @Test
    void movieExists_false() {
        when(movieRepository.existsById(2L)).thenReturn(false);
        assertFalse(movieService.movieExists(2L));
    }

    @Test
    void getMovieWithAvgGradeById_found() {
        Long id = 7L;
        Movie movie = new Movie();
        MovieWithAvgGradeDto dto = dummyDto("T");
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieMapper.movieToMovieWithAvgGradeDto(movie)).thenReturn(dto);

        MovieWithAvgGradeDto result = movieService.getMovieWithAvgGradeById(id);

        assertSame(dto, result);
    }

    @Test
    void getMovieWithAvgGradeById_notFound_throws() {
        when(movieRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.getMovieWithAvgGradeById(8L));
    }

    @Test
    void getAllMoviesWithAvgGradeDto_maps() {
        Pageable pageable = PageRequest.of(0, 2);
        Movie m1 = new Movie(); Movie m2 = new Movie();
        when(movieRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(m1, m2), pageable, 2));
        MovieWithAvgGradeDto d1 = dummyDto("A"); MovieWithAvgGradeDto d2 = dummyDto("B");
        when(movieMapper.movieToMovieWithAvgGradeDto(m1)).thenReturn(d1);
        when(movieMapper.movieToMovieWithAvgGradeDto(m2)).thenReturn(d2);

        Page<MovieWithAvgGradeDto> result = movieService.getAllMoviesWithAvgGradeDto(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("A", result.getContent().getFirst().getTitle());
    }

    @Test
    void searchMoviesWithAvgGradeByTitle_maps() {
        String title = "abc";
        Pageable pageable = PageRequest.of(0, 3);
        Movie m = new Movie();
        when(movieRepository.findByTitleIgnoreCaseContaining(title, pageable))
                .thenReturn(new PageImpl<>(List.of(m), pageable, 1));
        MovieWithAvgGradeDto dto = dummyDto("abc");
        when(movieMapper.movieToMovieWithAvgGradeDto(m)).thenReturn(dto);

        Page<MovieWithAvgGradeDto> result = movieService.searchMoviesWithAvgGradeByTitle(title, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("abc", result.getContent().getFirst().getTitle());
    }

    @Test
    void findMoviesByCategoryWithAvgGrade_maps() {
        String category = "Drama";
        Pageable pageable = PageRequest.of(0, 2);
        Movie m = new Movie();
        when(movieRepository.findMoviesByCategory(category, pageable))
                .thenReturn(new PageImpl<>(List.of(m), pageable, 1));
        MovieWithAvgGradeDto dto = dummyDto("C");
        when(movieMapper.movieToMovieWithAvgGradeDto(m)).thenReturn(dto);

        Page<MovieWithAvgGradeDto> result = movieService.findMoviesByCategoryWithAvgGrade(category, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getMoviesByTagWithAvgGrade_maps() {
        String tag = "thriller";
        Pageable pageable = PageRequest.of(0, 2);
        Movie m = new Movie();
        when(movieRepository.findByTagContaining(tag, pageable))
                .thenReturn(new PageImpl<>(List.of(m), pageable, 1));
        MovieWithAvgGradeDto dto = dummyDto("Tagged");
        when(movieMapper.movieToMovieWithAvgGradeDto(m)).thenReturn(dto);

        Page<MovieWithAvgGradeDto> result = movieService.getMoviesByTagWithAvgGrade(tag, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getMoviesByPolishPremiereMonthAndYearWithAvgGrade_maps() {
        int month = 5, year = 2024;
        Pageable pageable = PageRequest.of(0, 5);
        Movie m = new Movie();
        when(movieRepository.findMovieByPolishPremiereMonthAndYear(month, year, pageable))
                .thenReturn(new PageImpl<>(List.of(m), pageable, 1));
        MovieWithAvgGradeDto dto = dummyDto("PL");
        when(movieMapper.movieToMovieWithAvgGradeDto(m)).thenReturn(dto);

        Page<MovieWithAvgGradeDto> result =
                movieService.getMoviesByPolishPremiereMonthAndYearWithAvgGrade(month, year, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getMoviesByWorldPremiereMonthAndYearWithAvgGrade_maps() {
        int month = 6, year = 2023;
        Pageable pageable = PageRequest.of(0, 5);
        Movie m = new Movie();
        when(movieRepository.findMovieByWorldPremiereMonthAndYear(month, year, pageable))
                .thenReturn(new PageImpl<>((List.of(m)), pageable, 1));
        MovieWithAvgGradeDto dto = dummyDto("title");
        when(movieMapper.movieToMovieWithAvgGradeDto(m)).thenReturn(dto);

        Page<MovieWithAvgGradeDto> result =
                movieService.getMoviesByWorldPremiereMonthAndYearWithAvgGrade(month, year, pageable);

        assertEquals(1, result.getTotalElements());
    }

    private MovieWithAvgGradeDto dummyDto(String title) {
        return new MovieWithAvgGradeDto(
                title,
                2024,
                "Cat",
                "Desc",
                "Prizes",
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,2,2),
                "Tag",
                0,
                4.0
        );
    }
}