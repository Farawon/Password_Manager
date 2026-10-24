package org.example.password_manager_service.web.card;

import org.example.password_manager_service.service.cards_service.create.CreateCardsDto;
import org.example.password_manager_service.service.cards_service.create.CreateCardsUseCases;
import org.example.password_manager_service.service.cards_service.provider.CardsProviderUseCase;
import org.example.password_manager_service.service.cards_service.stripe.CardDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {
    private final CreateCardsUseCases createCardsUseCases;
    private final CardsProviderUseCase cardsProviderUseCase;

    public CardController(CreateCardsUseCases createCardsUseCases, CardsProviderUseCase cardsProviderUseCase) {
        this.createCardsUseCases = createCardsUseCases;
        this.cardsProviderUseCase = cardsProviderUseCase;
    }

    @SuppressWarnings("java:S2259")
    @PostMapping
    public CardDto addCard( @RequestBody  CreateCardsDto createCardsDto){
        return createCardsUseCases.createCards(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                createCardsDto
        );
    }

    @SuppressWarnings("java:S2259")
    @GetMapping
    public List<CardDto> userCard(){
        return  cardsProviderUseCase.providerCards(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
