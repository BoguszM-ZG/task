package com.tcode.moviebase.Exceptions;

public class ActorNotFoundException extends RuntimeException{
    public ActorNotFoundException(String message) {
        super(message);
    }
}
