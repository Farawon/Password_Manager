package org.example.password_manager_service.service.cards_service.create;

import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.cards_service.stripe.CardDto;
import org.example.password_manager_service.service.cards_service.stripe.StripePaymentMethodService;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCardsUseCases {
    private final UserRepository userRepository;
    private final StripePaymentMethodService stripePaymentMethodService;
    public CreateCardsUseCases(
            UserRepository userRepository, StripePaymentMethodService stripePaymentMethodService
    ) {
        this.userRepository = userRepository;
        this.stripePaymentMethodService = stripePaymentMethodService;
    }


    public CardDto createCards(String username, CreateCardsDto request){
        User user = userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
        String customerId = user.getStripeCustomerId();

        try{
            return stripePaymentMethodService.addCard(customerId, request.paymentMethodId());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
