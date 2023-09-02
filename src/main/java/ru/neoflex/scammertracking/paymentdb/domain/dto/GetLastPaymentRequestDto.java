package ru.neoflex.scammertracking.paymentdb.domain.dto;

public class GetLastPaymentRequestDto {

    public GetLastPaymentRequestDto(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public GetLastPaymentRequestDto() {
    }

    private String cardNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
