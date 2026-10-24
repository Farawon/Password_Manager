package org.example.password_manager_service.service.payment;

import com.stripe.model.Subscription;
import org.example.password_manager_service.entities.payment_audit.PaymentAuditLog;
import org.example.password_manager_service.entities.payment_audit.PaymentStatus;
import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.PaymentAuditLogRepository;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.cards_service.stripe.CardDto;
import org.example.password_manager_service.service.cards_service.stripe.StripePaymentMethodService;
import org.example.password_manager_service.shared.exceptions.CardExceptions;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private final StripePaymentSender stripePaymentSender;
    private final StripePaymentMethodService stripePaymentMethodService;
    private final PaymentFactory paymentFactory;
    private final PaymentAuditLogRepository paymentAuditLogRepository;
    public PaymentService(UserRepository userRepository, StripePaymentSender stripePaymentSender, StripePaymentMethodService stripePaymentMethodService, PaymentFactory paymentFactory, PaymentAuditLogRepository paymentAuditLogRepository) {
        this.userRepository = userRepository;
        this.stripePaymentSender = stripePaymentSender;
        this.stripePaymentMethodService = stripePaymentMethodService;
        this.paymentFactory = paymentFactory;
        this.paymentAuditLogRepository = paymentAuditLogRepository;
    }


    public PaymentStatusResponse createPayment(String email, CreatePaymentRequest request){
            User user= userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
            String stripeCustomerId = user.getStripeCustomerId();

            List<CardDto> userCards = stripePaymentMethodService.getCards(stripeCustomerId);

            CardDto userCard = userCards.stream()
                .filter(card -> card.getPmId().equals(request.paymentMethodId()))
                .findFirst()
                .orElseThrow(() -> new CardExceptions("Your card isn't working, please try again later"
                ));

            Subscription userSub = stripePaymentSender.createPayment(stripeCustomerId,userCard.getPmId(), request.plan());
            PaymentAuditLog paymentAuditLog = paymentFactory.createPaymentFactory(userCard.getPmId(), user, userSub.getId(), request.plan(), PaymentStatus.PENDING);
            paymentAuditLogRepository.save(paymentAuditLog);
            String clientSecret = null;

            if (userSub.getLatestInvoiceObject() != null
                    && userSub.getLatestInvoiceObject().getConfirmationSecret() != null) {

                clientSecret =
                        userSub.getLatestInvoiceObject()
                                .getConfirmationSecret()
                                .getClientSecret();
            }

        return paymentFactory.createPaymentStatusResponse(paymentAuditLog.getId(), paymentAuditLog.getPaymentStatus(), clientSecret);

    }
}
