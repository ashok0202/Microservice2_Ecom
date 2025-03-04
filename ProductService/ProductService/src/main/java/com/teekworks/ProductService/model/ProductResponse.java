package com.teekworks.ProductService.model;





public class ProductResponse {

    private String productName;
    private long productId;
    private long productPrice;
    private long productQuantity;


    public ProductResponse() {
    }

    public ProductResponse(String productName, long productId, long productPrice, long productQuantity) {
        this.productName = productName;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }


}
