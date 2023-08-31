package ru.neoflex.scammertracking.paymentdb.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.scammertracking.paymentdb.domain.dto.MessageInfoDto;
import ru.neoflex.scammertracking.paymentdb.error.exception.PaymentNotFoundException;
import ru.neoflex.scammertracking.paymentdb.utils.Constants;

@RestControllerAdvice
public class ErrorHandler {

    MessageInfoDto messageInfo = new MessageInfoDto(-1);

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MessageInfoDto handlePaymentNotFound(PaymentNotFoundException paymentNotFound) {
        messageInfo.setRespCode(Constants.NOT_FOUND);
        messageInfo.setMessage(paymentNotFound.getMessage());
        return messageInfo;
    }

}
