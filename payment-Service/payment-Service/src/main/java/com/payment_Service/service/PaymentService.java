package com.payment_Service.service;

import com.payment_Service.model.PaymentRequest;
import com.payment_Service.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
