package com.java.assignment.service.impl;

import static java.util.Objects.*;
import static com.java.assignment.util.CustomerUtil.*;

import com.google.gson.Gson;
import com.java.assignment.dto.OrderDetailsRequest;
import com.java.assignment.dto.OrderResponse;
import com.java.assignment.dto.RestResponse;
import com.java.assignment.exceptions.DetailsNotProvidedException;
import com.java.assignment.exceptions.OrderNotFoundException;
import com.java.assignment.exceptions.UserNotFoundException;
import com.java.assignment.model.Customer;
import com.java.assignment.model.Order;
import com.java.assignment.repository.CustomerRepository;
import com.java.assignment.repository.OrderRepository;
import com.java.assignment.service.OrderService;
import com.java.assignment.util.CustomerType;
import com.java.assignment.util.CustomerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<RestResponse> createAnOrder(OrderDetailsRequest orderDetailsRequest) {
        if (nonNull(orderDetailsRequest)) {
            if (nonNull(orderDetailsRequest.getCustomerId()) &&
                    !orderDetailsRequest.getCustomerId().toString().isBlank() &&
                    nonNull(orderDetailsRequest.getProductPrice())) {
                var customerFound = customerRepository.findById(orderDetailsRequest.getCustomerId());
                return customerFound.map((entity) -> {
                    var customerOrderCount = orderRepository.countByCustomerId(customerFound.get().getCustomerId()) + 1;
                    updateCustomerPackage(customerOrderCount, customerFound.get()); // checks if customer can be upgraded to new package
                    var customerType = entity.getCustomerType();
                    var discountOffered = CustomerType.getDiscountOffered(customerType);
                    var discountApplied = orderDetailsRequest.getProductPrice().multiply(new BigDecimal(discountOffered));
                    Gson gson = new Gson();
                    var orderRequestString = gson.toJson(orderDetailsRequest);
                    var order = gson.fromJson(orderRequestString, Order.class);
                    order.setOrderId(UUID.randomUUID());
                    order.setDiscountAmount(discountApplied);
                    order.setOrderDateTime(LocalDateTime.now());
                    orderRepository.save(order);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new RestResponse("Order created successfully.", 201, order));
                }).orElseThrow(() -> new UserNotFoundException("Customer doesn't exist.."));
            } else {
                throw new DetailsNotProvidedException("Customer id not provided...");
            }
        } else {
            throw new DetailsNotProvidedException("Order details not provided...");
        }
    }

    @Override
    public ResponseEntity<RestResponse> retrieveAnOrder(String orderId) {
        if (nonNull(orderId) && !orderId.isBlank()) {
            var orderFound = orderRepository.findById(UUID.fromString(orderId));
            return orderFound.map(order -> {
                if (nonNull(order.getCustomerId())) {
                    var customer = customerRepository.findById(order.getCustomerId());
                    var orderResponseObject = new OrderResponse(order, customer.orElse(null));
                    return ResponseEntity.ok().body(new RestResponse("Order found successfully.",
                            200, orderResponseObject));
                } else {
                    throw new OrderNotFoundException("No customer with order id : " + orderId + " found.");
                }
            }).orElseThrow(() -> new OrderNotFoundException("Order not found."));
        } else {
            throw new DetailsNotProvidedException("Order details not provided...");
        }
    }

    public void updateCustomerPackage(long customerOrderCount, Customer customerFound) {
        if (Optional.ofNullable(customerFound).isPresent()) {
           checkIfCustomerCanUpgradeTheirPlan(customerFound, customerOrderCount);

            if (customerOrderCount >= 10 && customerOrderCount <= 20 &&
                    getCustomerType(customerFound).equalsIgnoreCase(CustomerType.REGULAR.toString())) {
                customerFound.setCustomerType(CustomerType.GOLD.name());
                customerRepository.save(customerFound);
                return;
            }
            if (customerOrderCount >= 20) {
                customerFound.setCustomerType(CustomerType.PLATINUM.name());
                customerRepository.save(customerFound);
            }
        }
    }
}
