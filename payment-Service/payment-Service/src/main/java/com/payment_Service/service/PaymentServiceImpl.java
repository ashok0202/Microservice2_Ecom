package com.payment_Service.service;

import com.payment_Service.entity.TransactionalDetails;
import com.payment_Service.model.PaymentMode;
import com.payment_Service.model.PaymentRequest;
import com.payment_Service.model.PaymentResponse;
import com.payment_Service.repository.TransactionDetailsRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    @Transactional
    public long doPayment(PaymentRequest paymentRequest) {
        logger.info("Payment request {}", paymentRequest);

        TransactionalDetails transactionalDetails = new TransactionalDetails();


        transactionalDetails.setOrderId(paymentRequest.getOrderId());
        transactionalDetails.setPaymentMode(paymentRequest.getPaymentMode().name());
        transactionalDetails.setReferenceNumber(paymentRequest.getReferenceNumber());
        transactionalDetails.setPaymentDate(Instant.now());
        transactionalDetails.setPaymentStatus("SUCCESS");
        transactionalDetails.setAmount(paymentRequest.getAmount());

        transactionDetailsRepository.save(transactionalDetails);
        logger.info("Payment done for order id {}", paymentRequest.getOrderId());
        return transactionalDetails.getTransactionId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
        logger.info("Getting payment status for order id {}", orderId);

        TransactionalDetails transactionalDetails = transactionDetailsRepository.findByOrderId(orderId);
        PaymentResponse paymentResponse = new PaymentResponse();

        if(transactionalDetails != null) {
            paymentResponse.setPaymentId(transactionalDetails.getTransactionId());
            paymentResponse.setStatus(transactionalDetails.getPaymentStatus());
            paymentResponse.setPaymentMode(PaymentMode.valueOf(transactionalDetails.getPaymentMode()));
            paymentResponse.setAmount(transactionalDetails.getAmount());
            paymentResponse.setPaymentDate(transactionalDetails.getPaymentDate());
            paymentResponse.setOrderId(transactionalDetails.getOrderId());
        }

        return paymentResponse;
    }
}
