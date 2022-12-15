package com.java.assignment.service;

import com.java.assignment.dto.OrderDetailsRequest;
import com.java.assignment.dto.RestResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<RestResponse> createAnOrder(OrderDetailsRequest orderDetailsRequest);

    ResponseEntity<RestResponse> retrieveAnOrder(String orderId);
}
