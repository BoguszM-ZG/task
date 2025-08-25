//package com.tcode.moviebase.Services;
//
//import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
//import com.tcode.moviebase.Entities.Movie;
//import com.tcode.moviebase.Repositories.MovieRepository;
//import com.tcode.moviebase.Repositories.WatchedMoviesRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@Service
//@RequiredArgsConstructor
//public class MovieKidService {
//
//    private final MovieRepository movieRepository;
//    private final WatchedMoviesRepository watchedMoviesRepository;
//
//    public List<Movie> getMoviesForKids() {
//        return movieRepository.findAll().stream()
//                .filter(movie -> movie.getAgeRestriction() ==0)
//                .toList();
//    }
//
//    public Optional<?> getMovieByIdKids(Long id) {
//        Optional<Movie> movieOpt = movieRepository.findById(id);
//        if (movieOpt.isEmpty()) {
//            return Optional.of("Movie not found");
//        }
//        Movie movie = movieOpt.get();
//        if (movie.getAgeRestriction() != 0) {
//            return Optional.of("This movie is not suitable for kids");
//        }
//        return Optional.of(movie);
//    }
//
//    public List<Movie> findByCategoryForKids(String category) {
//        return movieRepository.findMoviesByCategory(category).stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<Movie> searchForKids(String search) {
//        return movieRepository.findByTitleIgnoreCaseContaining(search).stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<Movie> findMoviesForKidsByTag(String tag) {
//        return movieRepository.findByTagContaining(tag).stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//    public List<Movie> getMoviesByPremiereYearForKids(int premiereYear) {
//        return movieRepository.findAll().stream()
//                .filter(movie -> movie.getMovie_year() == premiereYear)
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<Movie> getMoviesByPolishPremiereMonthAndYearForKids(int month, int year) {
//        return movieRepository.findMovieByPolishPremiereMonthAndYear(month, year).stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<Movie> getMoviesByWorldPremiereMonthAndYearForKids(int month, int year) {
//        return movieRepository.findMovieByWorldPremiereMonthAndYear(month, year).stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getMoviesWithAvgGradeDescForKids() {
//        return movieRepository.findAllMoviesWithAvgGradeDesc().stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getMoviesWithAvgGradeAscForKids() {
//        return movieRepository.findAllMoviesWithAvgGradeAsc().stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getTopTenMoviesWithAvgGradeForKids() {
//        return movieRepository.findTop10MoviesByAvgGrade().stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getMoviesPropositionForKids(String userId) {
//        var movies = watchedMoviesRepository.findMoviesByUserId(userId);
//        List<String> categories = movies.stream()
//                .map(Movie::getCategory)
//                .distinct()
//                .toList();
//        return movieRepository.findMoviesPropositionByCategoriesDontIncludeWatchedMovies(categories, movies).stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//    public List<MovieWithAvgGradeDto> getAllMoviesForKidsWithAvgGrade() {
//        return movieRepository.findAllMoviesWithAvgGrade().stream()
//                .filter(movie -> movie.getAgeRestriction() == 0)
//                .toList();
//    }
//
//
//
//
//}
