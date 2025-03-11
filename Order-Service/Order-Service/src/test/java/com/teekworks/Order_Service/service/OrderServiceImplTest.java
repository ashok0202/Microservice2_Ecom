package com.teekworks.Order_Service.service;


import com.teekworks.Order_Service.entity.Order;
import com.teekworks.Order_Service.execption.CustomException;
import com.teekworks.Order_Service.external.client.PaymentService;
import com.teekworks.Order_Service.external.client.ProductService;
import com.teekworks.Order_Service.external.response.PaymentResponse;
import com.teekworks.Order_Service.model.OrderResponse;
import com.teekworks.Order_Service.model.PaymentMode;
import com.teekworks.Order_Service.repository.OrderRepository;
import com.teekworks.ProductService.model.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
        verify(orderRepository, Mockito.times(1)).findById(anyLong());
        verify(restTemplate, Mockito.times(1)).getForObject(
                "http://PRODUCT-SERVICE/product/getproduct/" + order.getProductId(), ProductResponse.class);
        verify(restTemplate, Mockito.times(1)).getForObject(
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
        verify(orderRepository, Mockito.times(1)).findById(anyLong());
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