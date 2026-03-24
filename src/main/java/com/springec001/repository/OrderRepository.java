package com.springec001.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springec001.entity.Order;

//OrderRepository.java
public interface OrderRepository extends CrudRepository<Order, Long> {
 List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
}