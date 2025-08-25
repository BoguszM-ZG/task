package com.tcode.moviebase.Services;


import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Mappers.MovieMapper;
import com.tcode.moviebase.Repositories.MovieRepository;
import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieJuniorService {

    private final MovieRepository movieRepository;
    private final WatchedMoviesRepository watchedMoviesRepository;
    private final MovieMapper movieMapper;

//    public List<Movie> getMoviesForJuniors() {
//        return movieRepository.findAll().stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public Optional<?> getMovieByIdJuniors(Long id) {
//        Optional<Movie> movieOpt = movieRepository.findById(id);
//        if (movieOpt.isEmpty()) {
//            return Optional.of("Movie not found");
//        }
//        Movie movie = movieOpt.get();
//        if (movie.getAgeRestriction() > 17) {
//            return Optional.of("This movie is not suitable for juniors");
//        }
//        return Optional.of(movie);
//    }
//
//    public List<Movie> findByCategoryForJuniors(String category) {
//        return movieRepository.findMoviesByCategory(category).stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<Movie> searchForJuniors(String search) {
//        return movieRepository.findByTitleIgnoreCaseContaining(search).stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<Movie> findMoviesForJuniorsByTag(String tag) {
//        return movieRepository.findByTagContaining(tag).stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//    public List<Movie> getMoviesByPremiereYearForJuniors(int premiereYear) {
//        return movieRepository.findAll().stream()
//                .filter(movie -> movie.getMovie_year() == premiereYear)
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<Movie> getMoviesByPolishPremiereMonthAndYearForJuniors(int month, int year) {
//        return movieRepository.findMovieByPolishPremiereMonthAndYear(month, year).stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<Movie> getMoviesByWorldPremiereMonthAndYearForJuniors(int month, int year) {
//        return movieRepository.findMovieByWorldPremiereMonthAndYear(month, year).stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getMoviesWithAvgGradeDescForJuniors() {
//        return movieRepository.findAllMoviesWithAvgGradeDesc().stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getMoviesWithAvgGradeAscForJuniors() {
//        return movieRepository.findAllMoviesWithAvgGradeAsc().stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getTopTenMoviesWithAvgGradeForJuniors() {
//        return movieRepository.findTop10MoviesByAvgGrade().stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }
//    public List<MovieWithAvgGradeDto> getMoviesPropositionForJuniors(String userId) {
//        var movies = watchedMoviesRepository.findMoviesByUserId(userId);
//        List<String> categories = movies.stream()
//                .map(Movie::getCategory)
//                .distinct()
//                .toList();
//        return movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies(categories, movies).stream()
//                .filter(movie -> movie.getAgeRestriction() <= 17)
//                .toList();
//    }

//    public Page<MovieWithAvgGradeDto> getAllMoviesWithAvgGradeForJuniors(Pageable pageable) {
//        Page<Movie> page = movieRepository.findAll(pageable);
//        List<MovieWithAvgGradeDto> content = page.getContent().stream()
//                .filter(m -> m.getAgeRestriction() <= 17)
//                .map(movieMapper::movieToMovieWithAvgGradeDto)
//                .toList();
//        return new PageImpl<>(content, pageable, content.size());
//    }
}
