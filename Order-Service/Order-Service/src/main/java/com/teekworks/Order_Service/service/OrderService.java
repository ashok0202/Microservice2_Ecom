package com.teekworks.Order_Service.service;

import com.teekworks.Order_Service.model.OrderRequest;
import com.teekworks.Order_Service.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrder(long orderId);
}
