package com.tcode.moviebase.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportMessageDto {
    private Long reportId;
    private String message;
    private String reason;
}
