package ru.neoflex.scammertracking.paymentdb.domain.dto;

public class GetLastPaymentRequestDto {

    public GetLastPaymentRequestDto(String payerCardNumber) {
        this.payerCardNumber = payerCardNumber;
    }

    public GetLastPaymentRequestDto() {
    }

    private String payerCardNumber;

    public String getPayerCardNumber() {
        return payerCardNumber;
    }

    public void setPayerCardNumber(String payerCardNumber) {
        this.payerCardNumber = payerCardNumber;
    }
}
