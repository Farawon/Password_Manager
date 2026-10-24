package org.example.password_manager_service.service.cards_service.provider;

import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.cards_service.stripe.CardDto;
import org.example.password_manager_service.service.cards_service.stripe.StripePaymentMethodService;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardsProviderUseCase {
    private final UserRepository userRepository;
    private final StripePaymentMethodService stripePaymentMethodService;
    public CardsProviderUseCase(UserRepository userRepository, StripePaymentMethodService stripePaymentMethodService) {
        this.userRepository = userRepository;
        this.stripePaymentMethodService = stripePaymentMethodService;

    }

    public List<CardDto> providerCards(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        return stripePaymentMethodService.getCards(user.getStripeCustomerId());
    }
}
