package ru.neoflex.scammertracking.paymentdb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.scammertracking.paymentdb.domain.dto.*;
import ru.neoflex.scammertracking.paymentdb.service.LogService;
import ru.neoflex.scammertracking.paymentdb.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/last-payment")
    public PaymentResponseDto getLastPaymentByReceiverCardNumber(@RequestBody GetLastPaymentRequestDto payment) {
        PaymentResponseDto responseDto = paymentService.getLastPayment(payment.getCardNumber());

        return responseDto;
    }

    @PostMapping("/save")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String savePayment(@RequestBody SavePaymentRequestDto payment) {
        paymentService.savePayment(payment);

        return "The payment was saved";
    }

    //    @Autowired
//    public PaymentController(CommonPaymentService commonPaymentService) {
//        this.commonPaymentService = commonPaymentService;
//    }
//
//    private CommonPaymentService commonPaymentService;

//    @PostMapping("/last-payment")
//    public PaymentResponseDto getLastPaymentByReceiverCardNumber(@RequestBody GetLastPaymentRequestDto payment) {
//        PaymentResponseDto responseDto = commonPaymentService.getLastPayment(payment.getCardNumber());
//
//        return responseDto;
//    }
//
//    @PostMapping
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public String createPayment(@RequestBody CreatePaymentRequestDto paymentRequest) {
//        commonPaymentService.insertPayments(paymentRequest.getIdCardNumber());
//        String response = "The rows were created";
//
//        return response;
//    }
//
//    @PutMapping
//    public UpdatePaymentResponseDto updatePayment(@RequestBody UpdatePaymentRequestDto updatePaymentRequest) {
//        UpdatePaymentResponseDto updateResponse = commonPaymentService.updatePayments(updatePaymentRequest);
//
//        return updateResponse;
//    }
}
