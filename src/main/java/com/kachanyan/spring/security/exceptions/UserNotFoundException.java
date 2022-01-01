package com.kachanyan.spring.security.exceptions;

public class UserNotFoundException extends RuntimeException {

    UserNotFoundException(Long id) {
        super("Could not fiind user " + id);
    }
}
