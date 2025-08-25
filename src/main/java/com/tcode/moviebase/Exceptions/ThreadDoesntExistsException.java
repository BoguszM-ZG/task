package com.tcode.moviebase.Exceptions;

public class ThreadDoesntExistsException extends RuntimeException{
    public ThreadDoesntExistsException(String message) {
        super(message);
    }
}
