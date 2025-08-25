package com.tcode.moviebase.Exceptions;

public class ForbiddenWordsException extends RuntimeException{
    public ForbiddenWordsException(String message){
        super(message);
    }
}
