package com.tcode.moviebase.Mappers;

import com.tcode.moviebase.Dtos.MovieWithAvgGradeDto;
import com.tcode.moviebase.Entities.Movie;
import com.tcode.moviebase.Entities.MovieGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "avgGrade", expression = "java(calculateAvgGrade(movie.getMovieGrades()))")
    MovieWithAvgGradeDto movieToMovieWithAvgGradeDto(Movie movie);

    default Double calculateAvgGrade(List<MovieGrade> movieGrades) {
        return movieGrades.stream()
                .mapToDouble(MovieGrade::getGrade)
                .average()
                .orElse(0.0);
    }
}
