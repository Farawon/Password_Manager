package org.example.password_manager_service.entities.payment_audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.password_manager_service.entities.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_audit_log", indexes = {
        @Index(name = "idx_stripe_subscription_id", columnList = "stripe_subscription_id")
})
@Setter
@Getter
public class PaymentAuditLog {

    @Id
    @GeneratedValue(generator = "uuid7")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;


    @Column(name = "stripe_subscription_id", nullable = false, unique = true)
    private String stripeSubscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "stripe_payment_method_id", nullable = false)
    private String stripePaymentMethodId;

    @Column(name = "plan")
    @Enumerated(EnumType.STRING)
    private Plan plan;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

}
