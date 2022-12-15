package com.java.assignment.dto;

import com.java.assignment.model.Customer;
import com.java.assignment.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@ToString
public class OrderResponse {

    private Order orderDetails;
    private Customer customer;
}
