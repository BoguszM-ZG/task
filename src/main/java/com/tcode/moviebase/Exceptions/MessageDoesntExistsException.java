package com.tcode.moviebase.Exceptions;

public class MessageDoesntExistsException extends RuntimeException{
    public MessageDoesntExistsException(String message){
        super(message);
    }
}
