package com.teekworks.ProductService.service;

import com.teekworks.ProductService.model.ProductRequest;
import com.teekworks.ProductService.model.ProductResponse;

public interface ProductService {

    long createProduct(ProductRequest productRequest);

    ProductResponse getProduct(long productId);

    void reduceQuantity(long productId, long quantity);
}
