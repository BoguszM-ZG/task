package com.tcode.moviebase.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieWithAvgGradeDto {
    private String title;
    private Integer movie_year;
    private String category;
    private String description;
    private String prizes;
    private LocalDate world_premiere;
    private LocalDate polish_premiere;
    private String tag;
    private Integer ageRestriction;
    private Double avgGrade;
}
