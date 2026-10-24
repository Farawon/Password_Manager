package org.example.password_manager_service.service.payment;

import jakarta.transaction.Transactional;
import org.example.password_manager_service.entities.payment_audit.PaymentAuditLog;
import org.example.password_manager_service.entities.payment_audit.PaymentStatus;
import org.example.password_manager_service.repository.PaymentAuditLogRepository;
import org.example.password_manager_service.shared.exceptions.PaymentAuditNotFound;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UpdatePayment {
    private final PaymentAuditLogRepository paymentAuditLogRepository;

    public UpdatePayment(PaymentAuditLogRepository paymentAuditLogRepository) {
        this.paymentAuditLogRepository = paymentAuditLogRepository;
    }

    public void updatePaymentAuditLog(PaymentStatus paymentStatus, String subscriptionId) {
        if (subscriptionId == null || subscriptionId.isEmpty()) {
            return;
        }
        
        PaymentAuditLog paymentAuditLog = paymentAuditLogRepository
                .findByStripeSubscriptionId(subscriptionId)
                .orElseThrow(() -> new PaymentAuditNotFound("User don't have audit payment"));
        paymentAuditLog.setPaymentStatus(paymentStatus);
    }
}