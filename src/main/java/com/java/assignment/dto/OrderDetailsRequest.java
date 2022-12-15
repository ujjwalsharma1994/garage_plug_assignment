package com.java.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Data
public class OrderDetailsRequest {
    private UUID customerId;
    private int productId;
    private String paymentMode;
    private BigDecimal productPrice;
}
