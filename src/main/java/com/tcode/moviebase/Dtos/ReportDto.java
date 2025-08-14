package com.tcode.moviebase.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private String userId;
    private Long commentId;
    private String comment;
    private String reason;
    private String createdAt;



}
