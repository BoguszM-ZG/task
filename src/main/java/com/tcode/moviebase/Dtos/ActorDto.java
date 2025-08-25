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
public class ActorDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private Integer height;
    private String biography;
    private Integer countOfPrizes;
    private Double avgGrade;
    private List<String> movieTitles;
}
