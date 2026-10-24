package org.example.password_manager_service.service.cards_service.stripe;

public class CardDto {

    private String pmId;
    private String maskedCard;
    private String expDate;

    public CardDto(String pmId, String maskedCard, String expDate) {
        this.pmId = pmId;
        this.maskedCard = maskedCard;
        this.expDate = expDate;
    }

    public String getPmId() { return pmId; }
    public String getMaskedCard() { return maskedCard; }
    public String getExpDate() { return expDate; }
}
