package ru.neoflex.scammertracking.paymentdb.domain.dto;

import ru.neoflex.scammertracking.paymentdb.domain.model.Coordinates;

import java.time.LocalDateTime;
import java.util.Date;

public class SavePaymentRequestDto {

    public SavePaymentRequestDto(long id, String payerCardNumber, String receiverCardNumber, Coordinates coordinates, Date date) {
        this.id = id;
        this.payerCardNumber = payerCardNumber;
        this.receiverCardNumber = receiverCardNumber;
        this.coordinates = coordinates;
        this.date = date;
    }

    public SavePaymentRequestDto() {
    }

    private long id;
    private String payerCardNumber;
    private String receiverCardNumber;
    private Coordinates coordinates;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPayerCardNumber() {
        return payerCardNumber;
    }

    public void setPayerCardNumber(String payerCardNumber) {
        this.payerCardNumber = payerCardNumber;
    }

    public String getReceiverCardNumber() {
        return receiverCardNumber;
    }

    public void setReceiverCardNumber(String receiverCardNumber) {
        this.receiverCardNumber = receiverCardNumber;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}