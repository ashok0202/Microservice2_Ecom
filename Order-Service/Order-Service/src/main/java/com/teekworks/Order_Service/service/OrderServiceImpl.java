package com.teekworks.Order_Service.service;

import com.teekworks.Order_Service.entity.Order;

import com.teekworks.Order_Service.execption.CustomException;
import com.teekworks.Order_Service.external.client.PaymentService;
import com.teekworks.Order_Service.external.client.ProductService;
import com.teekworks.Order_Service.external.request.PaymentRequest;
import com.teekworks.Order_Service.external.response.PaymentResponse;
import com.teekworks.Order_Service.model.OrderRequest;
import com.teekworks.Order_Service.model.OrderResponse;
import com.teekworks.Order_Service.repository.OrderRepository;
import com.teekworks.ProductService.model.ProductResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Override
    @Transactional
    public long placeOrder(OrderRequest orderRequest) {

        //Order Entity ->Save the Order with Status CREATED and Return the OrderId
        //Product Service -> Reduce the Quantity of the Product
        //Payment Service -> Make Payment Success -> COMPLETE ,ELSE -> FAIL

        logger.info("Placing the OrderRequest....");

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        logger.info("Created Order with status CREATED");
        Order order=new Order();
        order.setAmount(orderRequest.getTotalAmount());
        order.setOrderStatus("CREATED");
        order.setProductId(orderRequest.getProductId());
        order.setOrderDate(Instant.now());
        order.setQuantity(orderRequest.getQuantity());

        order=orderRepository.save(order);

        logger.info("Calling Payment Service to make Payment Complete");
        PaymentRequest paymentRequest=new PaymentRequest();

        paymentRequest.setOrderId(order.getOrderId());
        paymentRequest.setAmount(order.getAmount());
        paymentRequest.setReferenceNumber("123456");
        paymentRequest.setPaymentMode(orderRequest.getPaymentMode());

        String orderStatus=null;
        try {
            paymentService.doPayment(paymentRequest);
            logger.info("Payment Success");
            orderStatus="PLACED";
        } catch (Exception e) {
            logger.info("Error occures in payment .Changing the Order Status to PAYMENT_FAILED");
            orderStatus="PAYMENT_FAILED";
            throw new CustomException(e.getMessage(),"PAYMENT_FAILED",500);
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        logger.info("Order Placed successfully by OrderId {}",order.getOrderId());
        return order.getOrderId();
    }

    @Override
    public OrderResponse getOrder(long orderId) {
        logger.info("Fetching the Order with OrderId {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found with OrderId " + orderId, "ORDER_NOT_FOUND", 404));

        logger.info("Fetching the Product with ProductId {}", order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/getproduct/" + order.getProductId(), ProductResponse.class);

        logger.info("Getting payment Information from Payment Service");

        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/getPaymentStatus/" + order.getOrderId(), PaymentResponse.class
        );

        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        if (productResponse != null) {
            OrderResponse.ProductDetails productDetails = modelMapper.map(productResponse, OrderResponse.ProductDetails.class);
            orderResponse.setProductDetails(productDetails);
        } else {
            logger.warn("Product not found for ProductId {}", order.getProductId());
        }

        if (paymentResponse != null) {
            OrderResponse.PaymentDetails paymentDetails = new OrderResponse.PaymentDetails();
            paymentDetails.setPaymentId(paymentResponse.getPaymentId());
            paymentDetails.setStatus(paymentResponse.getStatus());
            paymentDetails.setPaymentDate(paymentResponse.getPaymentDate());
            paymentDetails.setPaymentMode(paymentResponse.getPaymentMode());
            paymentDetails.setAmount(paymentResponse.getAmount());

           // paymentDetails.setTransactionId(paymentResponse.getTransactionId());

            orderResponse.setPaymentDetails(paymentDetails);
        } else {
            logger.warn("Payment details not found for OrderId {}", order.getOrderId());
        }


        return orderResponse;
    }


}
