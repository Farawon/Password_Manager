package org.example.password_manager_service.service.cards_service.stripe.customer;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@SuppressWarnings("java:S5673")
public class StripeCustomerGateway {

    private final StripeClient stripeClient;

    public StripeCustomerGateway(@Value("${stripe.api.key}") String stripeApiKey) {
        this.stripeClient = new StripeClient(stripeApiKey);
    }

    public String createCustomerId(String email) {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();

        try {
            var customer = stripeClient.v1().customers().create(params);
            return customer.getId();
        } catch (StripeException e) {
            throw new RuntimeException("Error creating client in Stripe", e);
        }
    }
}
