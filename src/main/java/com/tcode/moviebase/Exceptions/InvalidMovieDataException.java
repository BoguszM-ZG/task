package com.tcode.moviebase.Exceptions;

public class InvalidMovieDataException extends RuntimeException{
    public InvalidMovieDataException(String message) {
        super(message);
    }
}
