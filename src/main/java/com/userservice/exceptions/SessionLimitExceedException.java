package com.userservice.exceptions;

public class SessionLimitExceedException  extends Exception{
    public SessionLimitExceedException(String message) {
        super(message);
    }
}
