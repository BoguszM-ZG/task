package com.tcode.moviebase.Exceptions;

public class MovieAlreadyInFavouritesException extends RuntimeException{
    public MovieAlreadyInFavouritesException(String message) {
        super(message);
    }
}
