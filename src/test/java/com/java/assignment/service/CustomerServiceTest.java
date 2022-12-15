package com.java.assignment.service;

import static org.mockito.Mockito.*;

import com.java.assignment.dto.CustomerDetailsRequest;
import com.java.assignment.exceptions.DetailsNotProvidedException;
import com.java.assignment.exceptions.UserNotFoundException;
import com.java.assignment.model.Customer;
import com.java.assignment.repository.CustomerRepository;
import com.java.assignment.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CustomerServiceTest {
    @Mock
    public CustomerRepository customerRepository;
    @InjectMocks
    public CustomerServiceImpl customerService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_customer_without_request_payload() {
        var exceptionCaught = Assertions.assertThrows(DetailsNotProvidedException.class,
                () -> customerService.createCustomer(null));
        Assertions.assertEquals("Customer details not provided...", exceptionCaught.getMessage());
    }

    @Test
    public void create_customer_with_valid_payload() {
        var customerDetailsRequest = createMockCustomerDetails();
        var response = customerService.createCustomer(customerDetailsRequest);
        Customer customer = (Customer) Objects.requireNonNull(response.getBody()).getData();
        Assertions.assertNotNull(customer);
        Assertions.assertEquals("Customer created successfully.", response.getBody().getMessage());
        Assertions.assertNotEquals(createMockCustomer(), customer);
    }

    @Test
    public void get_customer_with_valid_id() {
        var customerDetailsRequest = Optional.of(createMockCustomer());
        when(customerRepository.findById(any())).thenReturn(customerDetailsRequest);
        var response = customerService.getCustomerDetails(UUID.randomUUID().toString());
        Customer customer = (Customer) Objects.requireNonNull(response.getBody()).getData();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(customer);
        Assertions.assertEquals("1 Customer found.", response.getBody().getMessage());
        Assertions.assertEquals(200, response.getBody().getStatusCode());
    }

    @Test
    public void do_not_get_customer_with_invalid_id() {
        var customerId = UUID.randomUUID();
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        var exceptionCaught = Assertions.assertThrows(UserNotFoundException.class,
                () -> customerService.getCustomerDetails(customerId.toString()));
        Assertions.assertEquals("Customer id : " + customerId + " not present in our records.",
                exceptionCaught.getMessage());
    }

    @Test
    public void do_not_get_customer_with_blank_user_id() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        var exceptionCaught = Assertions.assertThrows(DetailsNotProvidedException.class,
                () -> customerService.getCustomerDetails(""));
        Assertions.assertEquals("Customer id not provided...",
                exceptionCaught.getMessage());
    }

    public static Customer createMockCustomer() {
        return new Customer(UUID.randomUUID(), "abc", "def", "xyz@gmail.com",
                "+10230381381", "REGULAR", LocalDate.now());
    }

    public static CustomerDetailsRequest createMockCustomerDetails() {
        return new CustomerDetailsRequest("xyz", "def", "xyz@gmail.com", "+10230381381");
    }
}
