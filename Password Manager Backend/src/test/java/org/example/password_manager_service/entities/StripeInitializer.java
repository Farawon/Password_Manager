package org.example.password_manager_service.entities;


import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;

// this is Stripe model, you must write API key in .env and this code you can start only  run once
public class StripeInitializer {
    public static void main(String[] args) throws StripeException {
        String apiKey = System.getenv("STRIPE_API_KEY");
        StripeClient stripeClient = new StripeClient(apiKey);

        ProductCreateParams firstProductParams =
                ProductCreateParams.builder()
                        .setName("Pro Subscription")
                        .setDescription("$9/Month subscription")
                        .build();
        Product product = stripeClient.v1().products().create(firstProductParams);
        System.out.println("Success! Here is your starter subscription product id: " + product.getId());

        ProductCreateParams secondProductParams =
                ProductCreateParams.builder()
                        .setName("Business Subscription")
                        .setDescription("$29/Month subscription")
                        .build();
        Product secondProduct = stripeClient.v1().products().create(secondProductParams);
        System.out.println("Success! Here is your starter subscription product id: " + secondProduct.getId());

        PriceCreateParams params =
                PriceCreateParams
                        .builder()
                        .setProduct(product.getId())
                        .setCurrency("usd")
                        .setUnitAmount(900L)
                        .setRecurring(
                                PriceCreateParams.Recurring
                                        .builder()
                                        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                        .build())
                        .build();
        Price price = stripeClient.v1().prices().create(params);
        System.out.println("Success! Here is your starter subscription price id: " + price.getId());

        PriceCreateParams secondParams =
                PriceCreateParams
                        .builder()
                        .setProduct(secondProduct.getId())
                        .setCurrency("usd")
                        .setUnitAmount(2900L)
                        .setRecurring(
                                PriceCreateParams.Recurring
                                        .builder()
                                        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                        .build())
                        .build();
        Price secondPrice = stripeClient.v1().prices().create(secondParams);
        System.out.println("Success! Here is your starter subscription price id: " + secondPrice.getId());
    }
}
