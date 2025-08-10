package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(Integer id);

    List<Order> findOrdersByStatusEqualsAndFarmerId(String status, Integer farmerId);

    List<Order> findOrdersByBuyerId(Integer buyerId);

    @Query("select COUNT(o.id) as number_of_orders, SUM(o.totalPrice) as revenue from Order o where o.farmerId=?1")
    String giveMeSalesSummaryByFarmerId(Integer farmerId);


}
