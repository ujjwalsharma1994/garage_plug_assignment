package com.java.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
@Document(collection = "order")
public class Order {
    @Id
    private UUID orderId;
    private UUID customerId;
    private int productId;
    private BigDecimal productPrice;
    private BigDecimal discountAmount;
    private String paymentMode;
    private LocalDateTime orderDateTime;
}
