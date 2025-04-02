package com.teekworks.Order_Service.external.client;


import com.teekworks.Order_Service.config.interceptur.FeignclientInterceptor;
import com.teekworks.Order_Service.execption.CustomException;
import com.teekworks.Order_Service.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CircuitBreaker(name = "external", fallbackMethod = "paymentServiceFallBack")
@FeignClient(name = "PAYMENT-SERVICE/payment", configuration = FeignclientInterceptor.class)
public interface PaymentService {

    @PostMapping("/doPayment")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void paymentServiceFallBack(Exception ex) {
        throw new CustomException("Payment Service is down","SERVICE_UNAVAILABLE",503);
    }
}
