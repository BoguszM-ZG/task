package com.tcode.moviebase.Exceptions;

public class UserIsNotMemberOfForumException extends RuntimeException{
    public UserIsNotMemberOfForumException(String message) {
        super(message);
    }
}
