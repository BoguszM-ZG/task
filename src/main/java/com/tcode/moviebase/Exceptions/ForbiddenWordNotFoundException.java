package com.tcode.moviebase.Exceptions;

public class ForbiddenWordNotFoundException extends RuntimeException{
    public ForbiddenWordNotFoundException(String message) {
        super(message);
    }
}
