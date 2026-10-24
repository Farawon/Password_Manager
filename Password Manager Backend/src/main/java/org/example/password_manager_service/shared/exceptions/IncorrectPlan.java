package org.example.password_manager_service.shared.exceptions;

public class IncorrectPlan extends RuntimeException {
    public IncorrectPlan(String message) {
        super(message);
    }
}
