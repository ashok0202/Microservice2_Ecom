package com.teekworks.Order_Service.controller;

import com.teekworks.Order_Service.model.OrderRequest;
import com.teekworks.Order_Service.model.OrderResponse;
import com.teekworks.Order_Service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;


    @PostMapping("/placeorder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {

        long orderId = orderService.placeOrder( orderRequest);
        logger.info( "Order placed successfully with id: " + orderId);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable long orderId) {

        OrderResponse orderResponse = orderService.getOrder(orderId);
        logger.info( "Order fetched successfully with id: " + orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }




}
