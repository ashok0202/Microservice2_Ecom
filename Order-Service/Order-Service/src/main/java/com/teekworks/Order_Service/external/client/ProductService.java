package com.teekworks.Order_Service.external.client;


import com.teekworks.Order_Service.execption.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external" , fallbackMethod = "productServiceFallBack")
@FeignClient(name = "PRODUCT-SERVICE/product" )
public interface ProductService {

    @PutMapping("/reduceQuantity/{productId}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable long productId, @RequestParam long quantity);

    default void productServiceFallBack(Exception ex) {
        throw new CustomException("Product Service is down","SERVICE_UNAVAILABLE",503);
    }


}
