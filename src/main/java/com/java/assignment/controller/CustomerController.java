package com.java.assignment.controller;

import com.java.assignment.dto.CustomerDetailsRequest;
import com.java.assignment.dto.RestResponse;
import com.java.assignment.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(path = "/create")
    public ResponseEntity<RestResponse> createCustomer(@RequestBody CustomerDetailsRequest
                                                       customerDetailsRequest) {
        return customerService.createCustomer(customerDetailsRequest);
    }

    @GetMapping(path = "/find/{customerId}")
    public ResponseEntity<RestResponse> getCustomerDetails(@PathVariable(name = "customerId") String customerId) {
        return customerService.getCustomerDetails(customerId);
    }

    @PutMapping(path = "/update/{customerId}")
    public ResponseEntity<RestResponse> createCustomer(@PathVariable(name = "customerId") String customerId,
                                                       @RequestBody CustomerDetailsRequest customerDetailsRequest) {
        return customerService.updateCustomerDetails(customerId, customerDetailsRequest);
    }

    @DeleteMapping(path = "/delete/{customerId}")
    public ResponseEntity<RestResponse> deleteCustomerDetails(@PathVariable(name = "customerId") String customerId) {
        return customerService.deleteCustomerDetails(customerId);
    }
}
