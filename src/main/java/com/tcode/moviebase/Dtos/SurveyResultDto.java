package com.tcode.moviebase.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SurveyResultDto {
    private String question;
    private String mostChosenAnswer;
}
