package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.FarmOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<FarmOrder, Integer> {
    FarmOrder findOrderById(Integer id);

    List<FarmOrder> findOrdersByStatusEqualsAndFarmerId(String status, Integer farmerId);

    List<FarmOrder> findOrdersByBuyerId(Integer buyerId);

    @Query("select COUNT(o.id) as number_of_orders, SUM(o.totalPrice) as revenue from FarmOrder o where o.farmerId=?1")
    String giveMeSalesSummaryByFarmerId(Integer farmerId);


}
