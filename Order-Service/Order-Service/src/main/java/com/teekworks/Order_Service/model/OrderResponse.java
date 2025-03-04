package com.teekworks.Order_Service.model;

import java.time.Instant;

public class OrderResponse {

    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;

    private ProductDetails productDetails;

    private PaymentDetails paymentDetails;


    public OrderResponse() {
    }

    public OrderResponse(long orderId, Instant orderDate, String orderStatus, long amount, ProductDetails productDetails, PaymentDetails paymentDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.amount = amount;
        this.productDetails = productDetails;
        this.paymentDetails = paymentDetails;
    }


    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }





    public static class ProductDetails {

        private String productName;
        private long productId;
        private long productPrice;
        private long productQuantity;


        public ProductDetails() {
        }

        public ProductDetails(String productName, long productId, long productPrice, long productQuantity) {
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

    public static class  PaymentDetails {

        private long paymentId;
        private String status;
        private PaymentMode paymentMode;
        private long amount;
        private Instant paymentDate;
        private String transactionId;

        public PaymentDetails() {
        }

        public PaymentDetails(long paymentId, String status, PaymentMode paymentMode, long amount, Instant paymentDate, String transactionId) {
            this.paymentId = paymentId;
            this.status = status;
            this.paymentMode = paymentMode;
            this.amount = amount;
            this.paymentDate = paymentDate;
            this.transactionId = transactionId;
        }

        public long getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(long paymentId) {
            this.paymentId = paymentId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public PaymentMode getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(PaymentMode paymentMode) {
            this.paymentMode = paymentMode;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public Instant getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(Instant paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }

}
