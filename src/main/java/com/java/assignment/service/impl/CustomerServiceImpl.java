package com.java.assignment.service.impl;

import static java.util.Objects.*;

import com.google.gson.Gson;
import com.java.assignment.dto.CustomerDetailsRequest;
import com.java.assignment.dto.RestResponse;
import com.java.assignment.exceptions.DetailsNotProvidedException;
import com.java.assignment.exceptions.UserNotFoundException;
import com.java.assignment.model.Customer;
import com.java.assignment.repository.CustomerRepository;
import com.java.assignment.service.CustomerService;
import com.java.assignment.util.CustomerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<RestResponse> createCustomer(CustomerDetailsRequest customerDetailsRequest) {
        if (nonNull(customerDetailsRequest)) {
            Gson gson = new Gson();
            String customerDetailsString = gson.toJson(customerDetailsRequest);
            Customer customerObject = gson.fromJson(customerDetailsString, Customer.class);
            customerObject.setCustomerId(UUID.randomUUID());
            customerObject.setCustomerType(CustomerType.REGULAR.name());
            customerObject.setCreationDate(LocalDate.now());
            customerRepository.save(customerObject);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RestResponse("Customer created successfully.", 201, customerObject));
        } else {
            throw new DetailsNotProvidedException("Customer details not provided...");
        }
    }

    @Override
    public ResponseEntity<RestResponse> getCustomerDetails(String id) {
        if (nonNull(id) && !id.isBlank()) {
            var customer = customerRepository.findById(UUID.fromString(id));
            return customer.map((customerEntity) -> ResponseEntity.ok()
                            .body(new RestResponse("1 Customer found.", 200, customerEntity)))
                    .orElseThrow(() -> new UserNotFoundException("Customer id : " + id + " not present in our records."));
        } else {
            throw new DetailsNotProvidedException("Customer id not provided...");
        }
    }

    @Override
    public ResponseEntity<RestResponse> updateCustomerDetails(String customerId,
                                                              CustomerDetailsRequest customerDetailsRequest) {
        if (nonNull(customerDetailsRequest) && nonNull(customerId)) {
            var customerObject = customerRepository.findById(UUID.fromString(customerId));
            if (customerObject.isPresent()) {
                if (nonNull(customerDetailsRequest.getFirstName())
                        && !customerDetailsRequest.getFirstName().isBlank()) {
                    customerObject.get().setFirstName(customerDetailsRequest.getFirstName());
                }
                if (nonNull(customerDetailsRequest.getLastName())
                        && !customerDetailsRequest.getLastName().isBlank()) {
                    customerObject.get().setLastName(customerDetailsRequest.getLastName());
                }
                if (nonNull(customerDetailsRequest.getEmail())
                        && !customerDetailsRequest.getEmail().isBlank()) {
                    customerObject.get().setEmail(customerDetailsRequest.getEmail());
                }
                if (nonNull(customerDetailsRequest.getPhone()) &&
                        !customerDetailsRequest.getPhone().isBlank()) {
                    customerObject.get().setPhone(customerDetailsRequest.getPhone());
                }
                customerRepository.save(customerObject.get());

                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new RestResponse("Customer updated successfully.", 204, customerObject));
            } else {
                throw new UserNotFoundException("Customer not found...");
            }
        } else {
            throw new DetailsNotProvidedException("Customer details not provided...");
        }
    }

    @Override
    public ResponseEntity<RestResponse> deleteCustomerDetails(String customerId) {
        if (nonNull(customerId) && !customerId.isBlank()) {
            customerRepository.deleteById(UUID.fromString(customerId));
            return ResponseEntity.ok().body(new RestResponse("1 Customer deleted.", 200, true));
        } else {
            throw new DetailsNotProvidedException("Customer id not provided...");
        }
    }
}
