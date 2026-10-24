package org.example.password_manager_service.web.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.net.Webhook;
import org.example.password_manager_service.entities.payment_audit.PaymentStatus;
import org.example.password_manager_service.service.payment.UpdatePayment;
import org.example.password_manager_service.service.user_settings.ChangeUserStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/stripe")
public class PaymentWebhookController {

    @Value("${stripe.webhook-secret}")
    private String secretKey;
    private final UpdatePayment updatePayment;
    private final ChangeUserStatus changeUserStatus;
    public PaymentWebhookController(UpdatePayment updatePayment, ChangeUserStatus changeUserStatus) {
        this.updatePayment = updatePayment;
        this.changeUserStatus = changeUserStatus;
    }

    private PaymentStatus getPaymentStatus(String eventType) {
        return switch (eventType) {
            case "invoice.payment_succeeded", "payment_intent.succeeded" -> PaymentStatus.SUCCESS;
            case "invoice.payment_failed", "payment_intent.payment_failed" -> PaymentStatus.FAILED;
            case "invoice.payment_pending", "payment_intent.requires_payment_method" -> PaymentStatus.PENDING;
            case "invoice.payment_canceled", "payment_intent.canceled" -> PaymentStatus.CANCELED;
            default -> null;
        };
    }

    @PostMapping
    public void webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, sigHeader, secretKey);
        String eventType = event.getType();

        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow();

        String subscriptionId = invoice
                .getParent()
                .getSubscriptionDetails()
                .getSubscription();

        PaymentStatus paymentStatus = getPaymentStatus(eventType);
        updatePayment.updatePaymentAuditLog(paymentStatus, subscriptionId);
        if(paymentStatus == PaymentStatus.SUCCESS){
            changeUserStatus.execute(invoice.getAmountDue(), invoice.getCustomer());
        }
    }

}