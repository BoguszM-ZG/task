package com.tcode.moviebase.Exceptions;

public class MovieAlreadyAssignedToDirectorException extends RuntimeException{
    public MovieAlreadyAssignedToDirectorException(String message) {
        super(message);
    }
}
