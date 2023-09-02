package ru.neoflex.scammertracking.paymentdb.exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
