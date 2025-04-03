package com.teekworks.Order_Service.service;


import com.teekworks.Order_Service.entity.Order;
import com.teekworks.Order_Service.execption.CustomException;
import com.teekworks.Order_Service.external.client.PaymentService;
import com.teekworks.Order_Service.external.client.ProductService;
import com.teekworks.Order_Service.external.request.PaymentRequest;
import com.teekworks.Order_Service.external.response.PaymentResponse;
import com.teekworks.Order_Service.model.OrderRequest;
import com.teekworks.Order_Service.model.OrderResponse;
import com.teekworks.Order_Service.model.PaymentMode;
import com.teekworks.Order_Service.repository.OrderRepository;
import com.teekworks.ProductService.model.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;


    @InjectMocks
    OrderService orderService=new OrderServiceImpl();


    @DisplayName("Get Orders Success scenario")
    @Test
    void then_When_Order_Success() {

        Order order = getMockOrder();
        OrderResponse orderResponse = new OrderResponse();

        // Mock ModelMapper Behavior
        when(modelMapper.map(order, OrderResponse.class)).thenReturn(orderResponse);

        //  Mock Repository Calls
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        //  Mock RestTemplate Calls
        when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/getproduct/" + order.getProductId(),
                ProductResponse.class)).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/getPaymentStatus/" + order.getOrderId(),
                PaymentResponse.class)).thenReturn(getMockPaymentResponse());

        // Actual Call
        OrderResponse result = orderService.getOrder(1);

        // Verification
        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                "http://PRODUCT-SERVICE/product/getproduct/" + order.getProductId(), ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(
                "http://PAYMENT-SERVICE/payment/getPaymentStatus/" + order.getOrderId(), PaymentResponse.class);

        // Assertions
        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
    }

    @DisplayName("Get Order Not Found")
    @Test
    void test_When_Get_Order_Not_Found() {

        // Mock Repository Calls
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        CustomException exception =assertThrows(CustomException.class,
                () -> orderService.getOrder(1));

        assertEquals("Order not found with OrderId 1", exception.getMessage());
        assertEquals("ORDER_NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        // Assertions
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Success() {
        // Arrange
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));

        // Act
        long orderId = orderService.placeOrder(orderRequest);

        // Assert
        verify(orderRepository, times(2)).save(any());
        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));

        assertEquals(order.getOrderId(), orderId);
    }

    @DisplayName("Place Order - Payment Failure Scenario")
    @Test
    void test_when_Place_Order_Fails_then_Exception_Thrown() {
        // Arrange
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Payment Failed"));

        // Act & Assert - Expect exception
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.placeOrder(orderRequest);
        });

        // Assert exception message
        assertEquals("Payment Failed", exception.getMessage());

        // Verify interactions
        verify(orderRepository, times(1)).save(any());
        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
    }




    private OrderRequest getMockOrderRequest() {

        OrderRequest orderRequest=new OrderRequest();
        orderRequest.setProductId(1);
        orderRequest.setQuantity(1);
        orderRequest.setTotalAmount(100);
        orderRequest.setPaymentMode(PaymentMode.CASH);
        return orderRequest;
    }

    private PaymentResponse getMockPaymentResponse() {

        PaymentResponse paymentResponse=new PaymentResponse();
        paymentResponse.setPaymentId(1);
        paymentResponse.setPaymentDate(Instant.now());
        paymentResponse.setPaymentMode(PaymentMode.CASH);
        paymentResponse.setStatus("SUCCESS");
        paymentResponse.setAmount(100);
        paymentResponse.setOrderId(1);

        return paymentResponse;
    }

    private ProductResponse getMockProductResponse() {

        ProductResponse productResponse=new ProductResponse();
        productResponse.setProductId(1);
        productResponse.setProductName("Mobile");
        productResponse.setProductQuantity(10);
        productResponse.setProductPrice(10);
        return productResponse;
    }

    private Order getMockOrder() {
        Order order=new Order();
        order.setAmount(100);
        order.setOrderStatus("CREATED");
        order.setProductId(1);
        order.setOrderDate(Instant.now());
        order.setQuantity(1);
        return order;
    }


}