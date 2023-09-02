package ru.neoflex.scammertracking.paymentdb.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.scammertracking.paymentdb.domain.dto.MessageInfoDto;
import ru.neoflex.scammertracking.paymentdb.exception.PaymentAlreadyExistsException;
import ru.neoflex.scammertracking.paymentdb.exception.PaymentNotFoundException;
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

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageInfoDto handlePaymentNotFound(PaymentAlreadyExistsException resourceExists) {
        messageInfo.setRespCode(Constants.BAD_REQUEST);
        messageInfo.setMessage(resourceExists.getMessage());
        return messageInfo;
    }
}