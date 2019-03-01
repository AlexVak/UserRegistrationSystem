package com.alexvak.urs.exceptions;

public class UserNotFoundException extends RuntimeException {

    private static final String NOT_FOND = "User not found. ID: %s";

    public UserNotFoundException(Long userId) {
        super(String.format(NOT_FOND, String.valueOf(userId)));
    }
}
