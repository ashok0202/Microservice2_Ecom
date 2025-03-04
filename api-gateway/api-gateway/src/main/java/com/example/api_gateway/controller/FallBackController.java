package com.example.api_gateway.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    public ResponseEntity<String> orderServiceFallBack(){
        String mess="Order Service is down";
        return new ResponseEntity<>(mess, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/productServiceFallBack")
    public ResponseEntity<String> productServiceFallBack(){
        String mesg="Product Service is down";
        return new ResponseEntity<>(mesg, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/paymentServiceFallBack")
    public ResponseEntity<String> paymentServiceFallBack(){
        String mess="Payment Service is down";
        return new ResponseEntity<>(mess, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
