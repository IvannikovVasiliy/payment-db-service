package ru.neoflex.scammertracking.paymentdb.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.scammertracking.paymentdb.domain.dto.GetLastPaymentRequestDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.PaymentResponseDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.SavePaymentRequestDto;
import ru.neoflex.scammertracking.paymentdb.domain.entity.PaymentEntity;
import ru.neoflex.scammertracking.paymentdb.domain.model.Coordinates;
import ru.neoflex.scammertracking.paymentdb.error.exception.PaymentNotFoundException;
import ru.neoflex.scammertracking.paymentdb.map.PaymentRowMapper;
import ru.neoflex.scammertracking.paymentdb.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    public PaymentServiceImpl(JdbcTemplate jdbcTemplate, ModelMapper modelMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.modelMapper = modelMapper;
    }

    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;

    @Override
    public PaymentResponseDto getLastPayment(GetLastPaymentRequestDto getLastPaymentRequestDto) {
        LOGGER.info("request. receiver card number={}", getLastPaymentRequestDto.getPayerCardNumber());

        String payerCardNumber = getLastPaymentRequestDto.getPayerCardNumber();
        String query = "select * from payments where payer_card_number=? and date=(select max(date) from payments where payer_card_number=?)";
        PaymentEntity paymentEntity = null;
        try {
            paymentEntity = jdbcTemplate.queryForObject(query, new PaymentRowMapper(), payerCardNumber, payerCardNumber);
        } catch (Exception e) {
            LOGGER.warn("Payer card number with id={} not found", payerCardNumber);
            String errorMessage = String.format("Payer card number with id=%s not found", payerCardNumber);
            throw new PaymentNotFoundException(errorMessage);
        }

        PaymentResponseDto response = modelMapper.map(paymentEntity, PaymentResponseDto.class);
        response.setCoordinates(new Coordinates(paymentEntity.getLatitude(), paymentEntity.getLongitude()));

        LOGGER.info("received. lastPayment={ id={}, payerCardNumber={}, receiverCardNumber={}, latitude={}, longitude={}, date ={} }",
                response.getId(), response.getPayerCardNumber(), response.getReceiverCardNumber(), response.getCoordinates().getLatitude(), response.getCoordinates().getLongitude(), response.getDate());

        return response;
    }

    @Override
    public boolean savePayment(SavePaymentRequestDto payment) {
        LOGGER.info("received. lastPayment={ id={}, payerCardNumber={}, receiverCardNumber={}, latitude={}, longitude={}, date ={} }",
                payment.getId(), payment.getPayerCardNumber(), payment.getReceiverCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());

        String query = "INSERT INTO payments VALUES(?,?,?,?,?,?)";
        try {
            jdbcTemplate.update(query, payment.getId(), payment.getPayerCardNumber(), payment.getReceiverCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());
        } catch (Exception e) {
            LOGGER.error("error. Cannot be saved the payment: {id={},payerCardNumber={},receiverCardNUmber={},latitude={}, longitude={}, date={} }",
                    payment.getId(), payment.getCoordinates(), payment.getReceiverCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());
            return false;
        }

        LOGGER.info("response. Save the payment: {id={},payerCardNumber={},receiverCardNUmber={},latitude={}, longitude={}, date={} }",
                payment.getId(), payment.getCoordinates(), payment.getReceiverCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());

        return true;
    }
}
