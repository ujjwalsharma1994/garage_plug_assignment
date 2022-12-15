package com.java.assignment.repository;

import com.java.assignment.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, UUID> {

    @Query(value = "{}", fields = "{_id: 1, firstName: 1, customerType: 1}")
    List<Customer> findAllCustomerIds();
}
