package com.tcode.moviebase.Exceptions;

public class MovieNotFoundInFavouritesException extends RuntimeException{
    public MovieNotFoundInFavouritesException(String message) {
        super(message);
    }
}
