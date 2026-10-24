package org.example.password_manager_service.shared.exceptions;

public class NotFoundCredential extends RuntimeException {
    public NotFoundCredential() {
        super("We can't found your credential, try again later");
    }
}
