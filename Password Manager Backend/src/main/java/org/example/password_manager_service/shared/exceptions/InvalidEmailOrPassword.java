package org.example.password_manager_service.shared.exceptions;

public class InvalidEmailOrPassword extends RuntimeException {
    public InvalidEmailOrPassword(String message) {
        super(message);
    }
}
