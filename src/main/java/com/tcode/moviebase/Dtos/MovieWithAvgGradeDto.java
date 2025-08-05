package com.tcode.moviebase.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MovieWithAvgGradeDto {
    private String title;
    private Integer movie_year;
    private String category;
    private String description;
    private String prizes;
    private LocalDate world_premiere;
    private LocalDate polish_premiere;
    private String tag;
    private Double avgGrade;
}
