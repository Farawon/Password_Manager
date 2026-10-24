package org.example.password_manager_service.shared.exceptions;

import com.stripe.exception.StripeException;

public class PaymentException extends RuntimeException {
    public PaymentException(String message, StripeException e) {
        super(message, e);
    }
}
