package org.example.password_manager_service.shared.exceptions;

public class NotFoundCard extends RuntimeException {
    public NotFoundCard() {

        super("Sorry we don't found cards, please add new card or try later");
    }
}
