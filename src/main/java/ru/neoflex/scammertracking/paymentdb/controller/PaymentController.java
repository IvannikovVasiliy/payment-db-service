package ru.neoflex.scammertracking.paymentdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.scammertracking.paymentdb.domain.dto.GetLastPaymentRequestDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.PaymentResponseDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.SavePaymentRequestDto;
import ru.neoflex.scammertracking.paymentdb.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private PaymentService paymentService;

    @PostMapping("/last-payment")
    public PaymentResponseDto getLastPaymentByReceiverCardNumber(@RequestBody GetLastPaymentRequestDto payment) {
        PaymentResponseDto responseDto = paymentService.getLastPayment(payment);

        return responseDto;
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String savePayment(@RequestBody SavePaymentRequestDto payment) {
        String response = String.format("The payment with id=%s was saved", paymentService.savePayment(payment));

        return response;
    }
}
