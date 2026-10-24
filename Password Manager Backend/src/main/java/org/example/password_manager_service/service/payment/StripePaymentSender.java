package org.example.password_manager_service.service.payment;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import org.example.password_manager_service.entities.payment_audit.Plan;
import org.example.password_manager_service.shared.exceptions.PaymentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentSender {

    private final StripeClient stripeClient;
    private final String priceIdPro;
    private final String priceIdBusiness;

    public StripePaymentSender(
            @Value("${stripe.api.key}") String stripeApiKey,
            @Value("${stripe.pro.subscription}") String priceIdPro,
            @Value("${stripe.business.subscription}") String priceIdBusiness
    ) {
        this.stripeClient = new StripeClient(stripeApiKey);
        this.priceIdPro = priceIdPro;
        this.priceIdBusiness = priceIdBusiness;
    }

    public Subscription createPayment(
            String stripeCustomerId,
            String paymentMethodId,
            Plan plan
    ) {

        try {
            String priceId = plan == Plan.PRO
                    ? priceIdPro
                    : priceIdBusiness;

            return stripeClient.v1().subscriptions().create(
                    SubscriptionCreateParams.builder()
                            .setCustomer(stripeCustomerId)
                            .setDefaultPaymentMethod(paymentMethodId)

                            // Важно для первого платежа подписки
                            .setPaymentBehavior(
                                    SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE
                            )

                            .addItem(
                                    SubscriptionCreateParams.Item.builder()
                                            .setPrice(priceId)
                                            .build()
                            )

                            // После успешной оплаты сохраняем PM
                            .setPaymentSettings(
                                    SubscriptionCreateParams.PaymentSettings.builder()
                                            .setSaveDefaultPaymentMethod(
                                                    SubscriptionCreateParams.PaymentSettings
                                                            .SaveDefaultPaymentMethod.ON_SUBSCRIPTION
                                            )
                                            .build()
                            )

                            // Получаем confirmation secret первого invoice
                            .addExpand("latest_invoice.confirmation_secret")

                            .build()
            );

        } catch (StripeException e) {
            throw new PaymentException("Payment failed", e);
        }
    }
}