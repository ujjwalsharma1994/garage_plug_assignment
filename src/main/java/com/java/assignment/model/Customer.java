package com.java.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
@Document(collection = "customer")
public class Customer {

    @Id
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String customerType;
    private LocalDate creationDate;

}
