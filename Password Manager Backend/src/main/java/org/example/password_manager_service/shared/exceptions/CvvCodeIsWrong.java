package org.example.password_manager_service.shared.exceptions;

public class CvvCodeIsWrong extends RuntimeException {
    public CvvCodeIsWrong() {
        super("CVV must be 3 digits");
    }
}
