package com.java.assignment.controller;

import com.java.assignment.dto.CustomerDetailsRequest;
import com.java.assignment.dto.OrderDetailsRequest;
import com.java.assignment.dto.RestResponse;
import com.java.assignment.service.CustomerService;
import com.java.assignment.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/create")
    public ResponseEntity<RestResponse> createOrder(@RequestBody OrderDetailsRequest
                                                       orderDetailsRequest) {

        return orderService.createAnOrder(orderDetailsRequest);
    }

    @GetMapping(path = "/find/{id}")
    public ResponseEntity<RestResponse> retrieveAnOrder(@PathVariable(name = "id") String id) {

        return orderService.retrieveAnOrder(id);
    }
}
