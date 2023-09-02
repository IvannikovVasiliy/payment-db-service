package ru.neoflex.scammertracking.paymentdb.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.scammertracking.paymentdb.domain.dto.PaymentResponseDto;
import ru.neoflex.scammertracking.paymentdb.domain.dto.SavePaymentRequestDto;
import ru.neoflex.scammertracking.paymentdb.domain.entity.PaymentEntity;
import ru.neoflex.scammertracking.paymentdb.domain.enums.DbAction;
import ru.neoflex.scammertracking.paymentdb.domain.model.Coordinates;
import ru.neoflex.scammertracking.paymentdb.exception.PaymentAlreadyExistsException;
import ru.neoflex.scammertracking.paymentdb.exception.PaymentNotFoundException;
import ru.neoflex.scammertracking.paymentdb.map.PaymentRowMapper;
import ru.neoflex.scammertracking.paymentdb.service.LogService;
import ru.neoflex.scammertracking.paymentdb.service.PaymentService;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    public PaymentServiceImpl(JdbcTemplate jdbcTemplate, ModelMapper modelMapper, LogService logService) {
        this.jdbcTemplate = jdbcTemplate;
        this.modelMapper = modelMapper;
        this.logService = logService;
    }

    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;
    private LogService logService;

    @Override
    public PaymentResponseDto getLastPayment(String cardNumber) {
        log.info("request. receiver card number={}", cardNumber);

        String query = "select * from payments where payer_card_number=? and date=(select max(date) from payments where payer_card_number=?) LIMIT 1";
        PaymentEntity paymentEntity = null;
        try {
            paymentEntity = jdbcTemplate.queryForObject(query, new PaymentRowMapper(), cardNumber, cardNumber);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Card number with id={} not found", cardNumber);
            String errorMessage = String.format("Payer card number with id=%s not found", cardNumber);
            throw new PaymentNotFoundException(errorMessage);
        }

        PaymentResponseDto response = modelMapper.map(paymentEntity, PaymentResponseDto.class);
        response.setCoordinates(new Coordinates(paymentEntity.getLatitude(), paymentEntity.getLongitude()));

        logService.insertLog(cardNumber, DbAction.SELECT, query);

        log.info("response. lastPayment={ id={}, payerCardNumber={}, receiverCardNumber={}, latitude={}, longitude={}, date={} }",
                response.getId(), response.getPayerCardNumber(), response.getReceiverCardNumber(), response.getCoordinates().getLatitude(), response.getCoordinates().getLongitude(), response.getDate());

        return response;
    }

    @Override
    public void savePayment(SavePaymentRequestDto payment) {
        log.info("received. lastPayment={ id={}, payerCardNumber={}, receiverCardNumber={}, latitude={}, longitude={}, date ={} }",
                payment.getId(), payment.getPayerCardNumber(), payment.getReceiverCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());

        String query = "INSERT INTO payments VALUES(?,?,?,?,?,?)";
        try {
            jdbcTemplate.update(query, payment.getId(), payment.getPayerCardNumber(), payment.getReceiverCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());
        } catch (DuplicateKeyException e) {
            String errorMessage = String.format("The payment with id=%s is already exist", payment.getId());
            log.error("error. {}", errorMessage);
            throw new PaymentAlreadyExistsException(errorMessage);
        } catch (Exception e) {
            log.error("error. Cannot be saved the payment: {id={},payerCardNumber={},receiverCardNUmber={},latitude={}, longitude={}, date={} }",
                    payment.getId(), payment.getCoordinates(), payment.getPayerCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());
        }

        log.info("response. Save the payment: {id={},payerCardNumber={},receiverCardNUmber={},latitude={}, longitude={}, date={} }",
                payment.getId(), payment.getCoordinates(), payment.getPayerCardNumber(), payment.getCoordinates().getLatitude(), payment.getCoordinates().getLongitude(), payment.getDate());

    }

    //    @Override
//    public PaymentResponseDto getLastPayment(Long id) {
//
//        PaymentEntity paymentEntity = jdbcTemplate.queryForObject("SELECT * FROM payments WHERE id=?", new PaymentRowMapper(), id);
//        PaymentResponseDto paymentResponse = modelMapper.map(paymentEntity, PaymentResponseDto.class);
//        paymentResponse.setCoordinates(new Coordinates(paymentEntity.getLatitude(), paymentEntity.getLongitude()));
//
//        return paymentResponse;
//    }
//
//    @Override
//    public void insertNullPayments(String idCardNumber) {
//        final String INSERT_NULL_PAYMENT = String.format("INSERT INTO payments(id_payment, id_card_number, latitude, longitude, date) VALUES(null, %s, null, null, null);", idCardNumber);
//
//        StringBuffer query = new StringBuffer();
//        query
//                .append("begin work;")
//                .append("lock table payments in row exclusive mode;");
//        for (int i = 0; i < Constants.MAX_COUNT_PAYMENTS_IN_DATABASE; i++) {
//            query.append(INSERT_NULL_PAYMENT);
//        }
//        query.append("end;");
//
//        jdbcTemplate.update(query.toString());
//    }

//    @Override
//    public UpdatePaymentResponseDto updatePayments(UpdatePaymentRequestDto updateRequest) {
//        jdbcTemplate.update("UPDATE TABLE payments SET id_payment=?, id_card_number=?, latitude=?, longitude=?, date=? WHERE id=?",
//                updateRequest.getIdPayment(), updateRequest.getCardNumber(), updateRequest.getCoordinates().getLatitude(), updateRequest.getCoordinates().getLongitude(), updateRequest.getDate(), id);
//
//        UpdatePaymentResponseDto updateResponse = modelMapper.map(updateRequest, UpdatePaymentResponseDto.class);
//
//        return updateResponse;
//    }
}
