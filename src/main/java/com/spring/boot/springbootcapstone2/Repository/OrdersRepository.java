package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Orders findOrdersById(Integer id);

    List<Orders> findOrdersByStatusEqualsAndFarmerId(String status, Integer farmerId);
}
