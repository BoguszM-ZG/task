package com.tcode.moviebase.Exceptions;

public class DirectorNotFoundException extends RuntimeException{
    public DirectorNotFoundException(String message) {
        super(message);
    }
}
