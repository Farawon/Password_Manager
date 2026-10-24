package org.example.password_manager_service.shared.exceptions;

public class PaymentAuditNotFound extends RuntimeException {
    public PaymentAuditNotFound(String message) {
        super(message);
    }
}
