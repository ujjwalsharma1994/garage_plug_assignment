package com.java.assignment.service;

import static org.mockito.Mockito.*;

import com.java.assignment.dto.OrderDetailsRequest;
import com.java.assignment.exceptions.DetailsNotProvidedException;
import com.java.assignment.exceptions.OrderNotFoundException;
import com.java.assignment.exceptions.UserNotFoundException;
import com.java.assignment.model.Customer;
import com.java.assignment.model.Order;
import com.java.assignment.repository.CustomerRepository;
import com.java.assignment.repository.OrderRepository;
import com.java.assignment.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class OrderServiceTest {

    @Mock
    public CustomerRepository customerRepository;
    @Mock
    public OrderRepository orderRepository;
    @InjectMocks
    public OrderServiceImpl orderServiceImpl;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void do_not_create_order_if_payload_not_present() {
        var exceptionCaught = Assertions.assertThrows(DetailsNotProvidedException.class,
                () -> orderServiceImpl.createAnOrder(null));
        Assertions.assertEquals("Order details not provided...", exceptionCaught.getMessage());
    }

    @Test
    public void do_not_create_order_if_customer_id_not_present() {
        var exceptionCaught = Assertions.assertThrows(DetailsNotProvidedException.class,
                () -> orderServiceImpl.createAnOrder(createMockOrderDetailsRequest(null)));
        Assertions.assertEquals("Customer id not provided...", exceptionCaught.getMessage());
    }

    @Test
    public void do_not_create_order_if_customer_do_not_exist_in_database() {
        var orderRequest = createMockOrderDetailsRequest(UUID.randomUUID());
        var exceptionCaught = Assertions.assertThrows(UserNotFoundException.class,
                () -> orderServiceImpl.createAnOrder(orderRequest));
        Assertions.assertEquals("Customer doesn't exist..", exceptionCaught.getMessage());
    }

    @Test
    public void create_order_if_customer_id_payload_is_valid() {
        var orderRequest = createMockOrderDetailsRequest(UUID.randomUUID());
        when(customerRepository.findById(any())).thenReturn(Optional.of(createMockCustomer()));
        when(orderRepository.countByCustomerId(any())).thenReturn(11L);
        var response = orderServiceImpl.createAnOrder(orderRequest);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertEquals("Order created successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void do_not_retrieve_order_if_order_id_is_not_present() {
        var exceptionCaught = Assertions.assertThrows(DetailsNotProvidedException.class,
                () -> orderServiceImpl.retrieveAnOrder("     "));
        Assertions.assertEquals("Order details not provided...", exceptionCaught.getMessage());
    }

    @Test
    public void do_not_retrieve_order_if_order_id_is_not_present_in_database() {
        var exceptionCaught = Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderServiceImpl.retrieveAnOrder(UUID.randomUUID().toString()));
        Assertions.assertEquals("Order not found.", exceptionCaught.getMessage());
    }

    @Test
    public void retrieve_order_if_order_id_is_valid() {
        var customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(createMockCustomer()));
        when(orderRepository.findById(any())).thenReturn(Optional.of(createMockOrder(customerId)));
        var response = orderServiceImpl.retrieveAnOrder(UUID.randomUUID().toString());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals("Order found successfully.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void do_not_retrieve_order_if_customer_id_not_associated_with_order() {
        var orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(createMockOrder(null)));
        var exceptionCaught = Assertions.assertThrows(OrderNotFoundException.class,
                () -> orderServiceImpl.retrieveAnOrder(orderId.toString()));
        Assertions.assertEquals("No customer with order id : " + orderId + " found.", exceptionCaught.getMessage());
    }

    public OrderDetailsRequest createMockOrderDetailsRequest(UUID customerId) {
        return new OrderDetailsRequest(customerId, 1, "COD", new BigDecimal(1000));
    }

    public Order createMockOrder(UUID customerId) {
        return new Order(UUID.randomUUID(), customerId, 1, new BigDecimal(1000),
                new BigDecimal(0),"COD", LocalDateTime.now());
    }

    public static Customer createMockCustomer() {
        return new Customer(UUID.randomUUID(), "abc", "def", "xyz@gmail.com",
                "10230381381", "REGULAR", LocalDate.now());
    }
}
