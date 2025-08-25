//package com.tcode.moviebase.Services;
//
//import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
//import com.tcode.moviebase.Entities.Movie;
//import com.tcode.moviebase.Repositories.MovieRepository;
//import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MovieKidServiceTest {
//
//    @Mock
//    private MovieRepository movieRepository;
//
//    @Mock
//    private WatchedMoviesRepository watchedMoviesRepository;
//
//
//    @InjectMocks
//    private MovieKidService movieKidService;
//
//    @Test
//    void testGetMoviesForKidsReturnsOnlyKidsMovies() {
//        Movie m1 = new Movie(); m1.setAgeRestriction(0);
//        Movie m2 = new Movie(); m2.setAgeRestriction(0);
//        Movie m3 = new Movie(); m3.setAgeRestriction(12);
//        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2, m3));
//
//        List<Movie> result = movieKidService.getMoviesForKids();
//
//        assertEquals(2, result.size());
//        assertTrue(result.stream().allMatch(m -> m.getAgeRestriction() == 0));
//    }
//
//    @Test
//    void testGetMovieByIdKidsReturnsMovieIfKids() {
//        Long id = 1L;
//        Movie movie = new Movie(); movie.setAgeRestriction(0);
//        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
//
//        Optional<?> result = movieKidService.getMovieByIdKids(id);
//
//        assertTrue(result.isPresent());
//        assertEquals(0, ((Movie) result.get()).getAgeRestriction());
//    }
//
//    @Test
//    void testGetMovieByIdKidsReturnsErrorIfNotKids() {
//        Long id = 2L;
//        Movie movie = new Movie(); movie.setAgeRestriction(12);
//        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
//
//        Optional<?> result = movieKidService.getMovieByIdKids(id);
//
//        assertTrue(result.isPresent());
//        assertEquals("This movie is not suitable for kids", result.get());
//    }
//
//    @Test
//    void testGetMovieByIdKidsReturnsErrorIfNotFound() {
//        Long id = 3L;
//        when(movieRepository.findById(id)).thenReturn(Optional.empty());
//
//        Optional<?> result = movieKidService.getMovieByIdKids(id);
//
//        assertTrue(result.isPresent());
//        assertEquals("Movie not found", result.get());
//    }
//
//    @Test
//    void testFindByCategoryForKidsReturnsOnlyKidsMovies() {
//        String category = "Animation";
//        Movie m1 = new Movie(); m1.setAgeRestriction(0); m1.setCategory(category);
//        Movie m2 = new Movie(); m2.setAgeRestriction(12); m2.setCategory(category);
//        when(movieRepository.findMoviesByCategory(category)).thenReturn(Arrays.asList(m1, m2));
//
//        List<Movie> result = movieKidService.findByCategoryForKids(category);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void testFindByTitleMoviesForKids() {
//        String title = "kids";
//        Movie m1 = new Movie(); m1.setAgeRestriction(0);
//        Movie m2 = new Movie(); m2.setAgeRestriction(15);
//        when(movieRepository.findByTitleIgnoreCaseContaining(title)).thenReturn(Arrays.asList(m1, m2));
//
//        List<Movie> result = movieKidService.searchForKids(title);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void testFindNewMoviesForKids() {
//        String tag = "fun";
//        Movie m1 = new Movie(); m1.setAgeRestriction(0); m1.setTag(tag);
//        Movie m2 = new Movie(); m2.setAgeRestriction(18); m2.setTag(tag);
//        when(movieRepository.findByTagContaining(tag)).thenReturn(Arrays.asList(m1, m2));
//
//        List<Movie> result = movieKidService.findMoviesForKidsByTag(tag);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void testGetMoviesByPremiereYearForKidsReturns() {
//        int year = 2023;
//        Movie m1 = new Movie(); m1.setAgeRestriction(0); m1.setMovie_year(year);
//        Movie m2 = new Movie(); m2.setAgeRestriction(12); m2.setMovie_year(year);
//        when(movieRepository.findAll()).thenReturn(Arrays.asList(m1, m2));
//
//        List<Movie> result = movieKidService.getMoviesByPremiereYearForKids(year);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void getMoviesByPolishPremiereMonthAndYearForKids() {
//        int month = 6;
//        int year = 2023;
//        Movie m1 = new Movie(); m1.setAgeRestriction(0);
//        Movie m2 = new Movie(); m2.setAgeRestriction(18);
//        when(movieRepository.findMovieByPolishPremiereMonthAndYear(month, year)).thenReturn(Arrays.asList(m1, m2));
//
//        List<Movie> result = movieKidService.getMoviesByPolishPremiereMonthAndYearForKids(month, year);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void getMoviesByWorldPremiereMonthAndYearForKids() {
//        int month = 5;
//        int year = 2023;
//        Movie m1 = new Movie(); m1.setAgeRestriction(0);
//        Movie m2 = new Movie(); m2.setAgeRestriction(15);
//        when(movieRepository.findMovieByWorldPremiereMonthAndYear(month, year)).thenReturn(Arrays.asList(m1, m2));
//
//        List<Movie> result = movieKidService.getMoviesByWorldPremiereMonthAndYearForKids(month, year);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void getMoviesWithAvgGradeDescForKids() {
//        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 0, 4.5);
//        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 12, 3.0);
//        when(movieRepository.findAllMoviesWithAvgGradeDesc()).thenReturn(Arrays.asList(m1, m2));
//
//        List<MovieWithAvgGradeDto> result = movieKidService.getMoviesWithAvgGradeDescForKids();
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void getMoviesWithAvgGradeAscForKids() {
//        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "asc", "prize", LocalDate.now(), LocalDate.now(), "tag", 0, 2.5);
//        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "asc", "prize", LocalDate.now(), LocalDate.now(), "tag", 18, 3.5);
//        when(movieRepository.findAllMoviesWithAvgGradeAsc()).thenReturn(Arrays.asList(m1, m2));
//
//        List<MovieWithAvgGradeDto> result = movieKidService.getMoviesWithAvgGradeAscForKids();
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//    @Test
//    void getTopTenMoviesWithAvgGradeForKids() {
//        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 0, 5.0);
//        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 16, 4.0);
//        when(movieRepository.findTop10MoviesByAvgGrade()).thenReturn(Arrays.asList(m1, m2));
//
//        List<MovieWithAvgGradeDto> result = movieKidService.getTopTenMoviesWithAvgGradeForKids();
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//    }
//
//
//
//    @Test
//    void testMoviesPropositionForKids() {
//        var userId = "user1";
//        var watched1 = new Movie();
//        watched1.setCategory("Animation");
//        var watched2 = new Movie();
//        watched2.setCategory("Family");
//        var watchedMovies = Arrays.asList(watched1, watched2);
//
//        var categories = Arrays.asList("Animation", "Family");
//        var prop1 = new MovieWithAvgGradeDto("A", 2023, "Animation", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 0, 4.5);
//        var prop2 = new MovieWithAvgGradeDto("B", 2023, "Family", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 12, 3.0);
//
//        when(watchedMoviesRepository.findMoviesByUserId(userId)).thenReturn(watchedMovies);
//        when(movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies((categories), (watchedMovies)))
//                .thenReturn(Arrays.asList(prop1, prop2));
//
//        var result = movieKidService.getMoviesPropositionForKids(userId);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//        verify(watchedMoviesRepository).findMoviesByUserId(userId);
//        verify(movieRepository).findMoviesPropositionByCategoriesDontIncludeWatchedMovies(categories, watchedMovies);
//    }
//
//    @Test
//    void testGetMoviesPropositionForKidsReturnsEmptyListWhenNoWatchedMovies() {
//        var userId = "user2";
//        when(watchedMoviesRepository.findMoviesByUserId(userId)).thenReturn(List.of());
//        when(movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies(List.of(), List.of()))
//                .thenReturn(List.of());
//
//        var result = movieKidService.getMoviesPropositionForKids(userId);
//
//        assertTrue(result.isEmpty());
//        verify(watchedMoviesRepository).findMoviesByUserId(userId);
//        verify(movieRepository).findMoviesPropositionByCategoriesDontIncludeWatchedMovies(List.of(), List.of());
//    }
//
//    @Test
//    void testGetMoviesPropositionForKidsReturnsEmptyListWhenNoPropositions() {
//        var userId = "user3";
//        var watched = new Movie(); watched.setCategory("Adventure");
//        var watchedMovies = List.of(watched);
//        var categories = List.of("Adventure");
//
//        when(watchedMoviesRepository.findMoviesByUserId(userId)).thenReturn(watchedMovies);
//        when(movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies(categories, watchedMovies))
//                .thenReturn(List.of());
//
//        var result = movieKidService.getMoviesPropositionForKids(userId);
//
//        assertTrue(result.isEmpty());
//        verify(watchedMoviesRepository).findMoviesByUserId(userId);
//        verify(movieRepository).findMoviesPropositionByCategoriesDontIncludeWatchedMovies(categories, watchedMovies);
//    }
//
//    @Test
//    void testGetAllMoviesForKidsWithAvgGrade() {
//        MovieWithAvgGradeDto m1 = new MovieWithAvgGradeDto("A", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 0, 4.5);
//        MovieWithAvgGradeDto m2 = new MovieWithAvgGradeDto("B", 2023, "cat", "desc", "prize", LocalDate.now(), LocalDate.now(), "tag", 12, 3.0);
//        when(movieRepository.findAllMoviesWithAvgGrade()).thenReturn(Arrays.asList(m1, m2));
//
//        List<MovieWithAvgGradeDto> result = movieKidService.getAllMoviesForKidsWithAvgGrade();
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.getFirst().getAgeRestriction());
//        verify(movieRepository).findAllMoviesWithAvgGrade();
//    }
//}