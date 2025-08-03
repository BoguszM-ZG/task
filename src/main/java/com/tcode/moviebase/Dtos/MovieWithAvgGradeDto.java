package com.tcode.moviebase.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieWithAvgGradeDto {
    private String title;
    private Integer movie_year;
    private String category;
    private String description;
    private String prizes;
    private Double avgGrade;
}
