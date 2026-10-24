package org.example.password_manager_service.service.payment;

import org.example.password_manager_service.entities.payment_audit.PaymentAuditLog;
import org.example.password_manager_service.entities.payment_audit.PaymentStatus;
import org.example.password_manager_service.entities.payment_audit.Plan;
import org.example.password_manager_service.entities.user.User;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class PaymentFactory {
    public PaymentAuditLog createPaymentFactory(
            String stripePaymentMethodId,
            User user,
            String stripePaymentIntentId,
            Plan plan,
            PaymentStatus paymentStatus
          ){
        PaymentAuditLog paymentAuditLog = new PaymentAuditLog();
        paymentAuditLog.setStripePaymentMethodId(stripePaymentMethodId);
        paymentAuditLog.setUser(user);
        paymentAuditLog.setStripeSubscriptionId(stripePaymentIntentId);
        paymentAuditLog.setPlan(plan);
        paymentAuditLog.setPaymentStatus(paymentStatus);
        return paymentAuditLog;
    }
    public PaymentStatusResponse createPaymentStatusResponse(UUID paymentId, PaymentStatus paymentStatus,
                                                             String clientSecret){
        return new PaymentStatusResponse(
                paymentId,
                paymentStatus,
                clientSecret
        );
    }
}
