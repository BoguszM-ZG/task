package com.tcode.moviebase.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SurveyDto {
    private String title;
    private List<SurveyQuestionDto> questions;
}
