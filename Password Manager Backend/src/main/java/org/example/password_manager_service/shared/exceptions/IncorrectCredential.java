package org.example.password_manager_service.shared.exceptions;

public class IncorrectCredential extends RuntimeException {
    public IncorrectCredential() {
        super("Your credential is incorrect maybe you don't have this credential");
    }
}
