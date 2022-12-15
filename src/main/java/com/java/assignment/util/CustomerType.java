package com.java.assignment.util;

public enum CustomerType {
    REGULAR,
    GOLD,
    PLATINUM;

    public static double getDiscountOffered(String customerType) {
        return customerType.equalsIgnoreCase(GOLD.toString()) ?
                0.1 : customerType.equalsIgnoreCase(PLATINUM.toString()) ?
                0.2 : 0;
    }
}
