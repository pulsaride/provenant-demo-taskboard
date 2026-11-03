package com.github.hantsy.ee8sample.service;

public class InvalidUsernameException extends RuntimeException {

    public InvalidUsernameException(String message) {
        super(message + " is invalid");
    }
}
