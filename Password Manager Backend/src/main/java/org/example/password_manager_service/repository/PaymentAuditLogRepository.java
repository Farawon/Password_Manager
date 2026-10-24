package org.example.password_manager_service.repository;

import org.example.password_manager_service.entities.payment_audit.PaymentAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentAuditLogRepository extends JpaRepository<PaymentAuditLog, UUID> {
    Optional<PaymentAuditLog> findByStripeSubscriptionId(String subscriptionId);
}
