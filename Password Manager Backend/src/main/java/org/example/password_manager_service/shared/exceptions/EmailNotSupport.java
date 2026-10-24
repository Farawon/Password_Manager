package org.example.password_manager_service.shared.exceptions;

public class EmailNotSupport extends RuntimeException {
    public EmailNotSupport() {
        super("We don't support this email");
    }
}
