package org.example.password_manager_service.service.cards_service.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodListParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StripePaymentMethodService {
    private final StripeClient stripeClient;

    public StripePaymentMethodService(@Value("${stripe.api.key}") String stripeApiKey) {
        this.stripeClient = new StripeClient(stripeApiKey);
    }

    public List<CardDto> getCards(String customerId) {
        PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setCustomer(customerId)
                .setType(PaymentMethodListParams.Type.CARD)
                .setLimit(10L)
                .build();

        try {
            StripeCollection<PaymentMethod> paymentMethods = stripeClient.v1().paymentMethods().list(params);
            return paymentMethods.getData().stream().map(
                    pm -> {
                        PaymentMethod.Card card = pm.getCard();
                        String pmId = pm.getId();
                        String maskedCard = card.getLast4();
                        String expDate = String.format("%02d/%d", card.getExpMonth(), card.getExpYear());
                        return new CardDto(pmId, maskedCard, expDate);
                    }
            ).toList();
        } catch (StripeException e) {
            throw new RuntimeException("Error retrieving payment methods from Stripe", e);
        }
    }
    public CardDto addCard(String customerId, String paymentMethodId) {
        try{
            PaymentMethodAttachParams params =
                    PaymentMethodAttachParams.builder()
                            .setCustomer(customerId)
                            .build();

            PaymentMethod paymentMethod =
                    stripeClient.v1()
                            .paymentMethods()
                            .attach(paymentMethodId, params);

            PaymentMethod.Card card = paymentMethod.getCard();

            String expDate = String.format(
                    "%02d/%d",
                    card.getExpMonth(),
                    card.getExpYear()
            );

            return new CardDto(
                    paymentMethod.getId(),
                    card.getLast4(),
                    expDate
            );
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
