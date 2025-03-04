package com.teekworks.ProductService.controller;

import com.teekworks.ProductService.model.ProductRequest;
import com.teekworks.ProductService.model.ProductResponse;
import com.teekworks.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/addproduct")
    public ResponseEntity<Long> createProduct( @RequestBody ProductRequest productRequest) {
        long productId = productService.createProduct(productRequest);

        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping("/getproduct/{productId}")
    public ResponseEntity<ProductResponse> getProduct( @PathVariable long productId) {
        ProductResponse productResponse = productService.getProduct(productId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/reduceQuantity/{productId}")
    public ResponseEntity<Void> reduceQuantity( @PathVariable long productId,@RequestParam long quantity) {

        productService.reduceQuantity(productId,quantity);

        return new ResponseEntity<>(HttpStatus.OK);

    }




}
