package com.oneenterprise.orderservice.dto;

import java.math.BigDecimal;


public class OrderResponseDto {

    private Long orderId;
    private String product;
    private BigDecimal amount;
    private UserSummaryDto user;

    public OrderResponseDto(Long orderId, String product, BigDecimal amount, UserSummaryDto user) {
        this.orderId = orderId;
        this.product = product;
        this.amount = amount;
        this.user = user;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getProduct() {
        return product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UserSummaryDto getUser() {
        return user;
    }
}
