package com.tcode.moviebase.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String biography;
    private String gender;
    private Double rating;
    private List<String> movieTitles;
}
