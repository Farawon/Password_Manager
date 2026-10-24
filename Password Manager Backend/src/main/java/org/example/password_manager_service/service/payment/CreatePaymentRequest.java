package org.example.password_manager_service.service.payment;

import jakarta.validation.constraints.NotBlank;
import org.example.password_manager_service.entities.payment_audit.Plan;

public record CreatePaymentRequest(
        @NotBlank
        String paymentMethodId,

        @NotBlank
        Plan plan
) {
}
