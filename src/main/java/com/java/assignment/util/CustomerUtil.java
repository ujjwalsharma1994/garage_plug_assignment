package com.java.assignment.util;

import com.java.assignment.model.Customer;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CustomerUtil {

    public static String getCustomerType(Customer customer) {
        return Optional.ofNullable(customer)
                .map(Customer::getCustomerType)
                .orElse(null);
    }
    public static void checkIfCustomerCanUpgradeTheirPlan(Customer customer, long customerOrderCount) {
        if (customer.getCustomerType().equalsIgnoreCase(CustomerType.REGULAR.toString()) ||
                customer.getCustomerType().equalsIgnoreCase(CustomerType.GOLD.toString()))
            if (customerOrderCount < 10 && (10 - customerOrderCount) == 1) {
                sendAlertToCustomerForGoldPlan(customer);
                return;
            }
            if (customerOrderCount > 10 && (20 - customerOrderCount) == 1) {
                sendAlertToCustomerForPlatinumPlan(customer);
            }
    }

    public static void sendAlertToCustomerForGoldPlan(Customer customer) {
        log.info(customer.getFirstName() + ", you have placed 9 orders with us. Buy one more stuff and you will be " +
                "promoted to Gold customer and enjoy 10% discounts!");
    }

    public static void sendAlertToCustomerForPlatinumPlan(Customer customer) {
        log.info(customer.getFirstName() + ", you have placed 19 orders with us. Buy one more stuff and you will be " +
                "promoted to Platinum customer and enjoy 20% discounts!");
    }
}
