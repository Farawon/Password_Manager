package org.example.password_manager_service.service.payment;

import org.example.password_manager_service.entities.payment_audit.PaymentStatus;

import java.util.UUID;

public record PaymentStatusResponse(
        UUID paymentMethodId,
        PaymentStatus paymentStatus,
        String clientSecret
) {
}
