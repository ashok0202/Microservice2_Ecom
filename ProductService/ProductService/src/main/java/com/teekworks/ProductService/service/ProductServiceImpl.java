package com.teekworks.ProductService.service;


import com.teekworks.ProductService.entity.Product;
import com.teekworks.ProductService.exception.ProductServiceCustomException;
import com.teekworks.ProductService.model.ProductRequest;
import com.teekworks.ProductService.model.ProductResponse;
import com.teekworks.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Logger LOGGER= LoggerFactory.getLogger(ProductServiceImpl.class);


    @Override
    @Transactional
    public long createProduct(ProductRequest productRequest) {
        LOGGER.info("Adding product ...");

        Product product = modelMapper.map(productRequest, Product.class);

        productRepository.save(product);
        LOGGER.info("Product added successfully");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProduct(long productId) {
        LOGGER.info("Fetching product ...");

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductServiceCustomException("Product Not found with id: " + productId,"PRODUCT_NOT_FOUND"));

        return modelMapper.map(product, ProductResponse.class);

    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        LOGGER.info("Reducing quantity{} for product {}",quantity,productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductServiceCustomException("Product Not found with id: " + productId,"PRODUCT_NOT_FOUND"));

        if(product.getProductQuantity() < quantity){
            throw new ProductServiceCustomException("Product quantity not sufficient","PRODUCT_QUANTITY_NOT_SUFFICIENT");
        }
        product.setProductQuantity(product.getProductQuantity() - quantity);

        productRepository.save(product);
        LOGGER.info("Product quantity reduced successfully");
    }
}
