package com.java.assignment.service.impl;

import static com.java.assignment.util.CustomerUtil.*;

import com.java.assignment.model.Customer;
import com.java.assignment.repository.CustomerRepository;
import com.java.assignment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CronJobTask {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = 86400000/2)
    public void sendNotificationToUserForUpgradingPlan() {
        List<Customer> customerList = customerRepository.findAllCustomerIds(); //find all customers

        for (Customer eachCustomer : customerList) { // check order count for each customer and then send notification
            var customerOrderCount = orderRepository.countByCustomerId(eachCustomer.getCustomerId());
            checkIfCustomerCanUpgradeTheirPlan(eachCustomer, customerOrderCount); //check and send notification
        }
    }
}
