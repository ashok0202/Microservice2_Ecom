package com.payment_Service.controller;

import com.payment_Service.model.PaymentRequest;
import com.payment_Service.model.PaymentResponse;
import com.payment_Service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymetController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/doPayment")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        long transactionId = paymentService.doPayment(paymentRequest);
        return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
    }

    @GetMapping("/getPaymentStatus/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable long orderId) {
        PaymentResponse paymentResponse = paymentService.getPaymentDetailsByOrderId(orderId);
        return  new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
}
