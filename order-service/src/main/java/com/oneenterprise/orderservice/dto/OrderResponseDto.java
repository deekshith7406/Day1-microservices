package com.oneenterprise.orderservice.dto;

import java.math.BigDecimal;

/**
 * What the client of Order Service sees. Combines the order's own data
 * with the user summary Order Service fetched from User Service over HTTP —
 * this is the proof that the two services actually talked to each other.
 */
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
