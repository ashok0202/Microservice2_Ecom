package com.teekworks.Order_Service.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "`order`")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    @Column(name = "PRODUCT_ID", nullable = false)
    private long productId;

    @Column(name = "QUANTITY", nullable = false)
    private long quantity;
    @Column(name = "ORDER_DATE", nullable = false)
    private Instant orderDate;

    @Column(name = "STATUS", nullable = false)
    private String orderStatus;
    @Column(name = "TOTAL_AMOUNT", nullable = false)
    private long amount;

    public Order() {
    }

    public Order(long orderId, long productId, long quantity, Instant orderDate, String orderStatus, long amount) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.amount = amount;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
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






}
