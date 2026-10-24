package org.example.password_manager_service.service.cards_service.create;

import jakarta.validation.constraints.*;

public record CreateCardsDto(

    @NotBlank
    String paymentMethodId
){
}
