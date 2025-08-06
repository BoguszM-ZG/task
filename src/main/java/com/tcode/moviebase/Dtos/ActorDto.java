package com.tcode.moviebase.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ActorDto {
    private String gender;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private Integer height;
    private String biography;
    private Integer countOfPrizes;
    private Double avgGrade;


}
