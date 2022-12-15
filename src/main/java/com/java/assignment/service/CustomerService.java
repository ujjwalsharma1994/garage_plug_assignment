package com.java.assignment.service;

import com.java.assignment.dto.CustomerDetailsRequest;
import com.java.assignment.dto.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {

    ResponseEntity<RestResponse> createCustomer(CustomerDetailsRequest customerDetailsRequest);

    ResponseEntity<RestResponse> getCustomerDetails(String id);

    ResponseEntity<RestResponse> updateCustomerDetails(String customerId,
                                                       CustomerDetailsRequest customerDetailsRequest);

    ResponseEntity<RestResponse> deleteCustomerDetails(String customerId);
}
