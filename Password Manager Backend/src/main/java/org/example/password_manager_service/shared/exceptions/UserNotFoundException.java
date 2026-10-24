package org.example.password_manager_service.shared.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super( "We can't find user, try later or login");
    }
}
