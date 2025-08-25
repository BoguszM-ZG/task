package com.tcode.moviebase.Exceptions;

public class GradeOutOfRangeException extends RuntimeException{
    public GradeOutOfRangeException(int grade) {
        super("Grade " + grade + " is out of range. It must be between 1 and 10.");
    }
}
