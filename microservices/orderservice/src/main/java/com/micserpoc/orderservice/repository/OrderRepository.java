package com.micserpoc.orderservice.repository;

import com.micserpoc.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>
{
}
