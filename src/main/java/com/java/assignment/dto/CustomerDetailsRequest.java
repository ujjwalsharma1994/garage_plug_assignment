package com.java.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
public class CustomerDetailsRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
