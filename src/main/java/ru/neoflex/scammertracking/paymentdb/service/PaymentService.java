package ru.neoflex.scammertracking.paymentdb.service;

import ru.neoflex.scammertracking.paymentdb.domain.dto.GetLastPaymentRequestDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.PaymentResponseDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.SavePaymentRequestDto;

public interface PaymentService {
    PaymentResponseDto getLastPayment(GetLastPaymentRequestDto payment);
    boolean savePayment(SavePaymentRequestDto payment);
}
