package com.java.assignment.repository;

import com.java.assignment.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends MongoRepository<Order, UUID> {

    Long countByCustomerId(UUID customerId);
}
