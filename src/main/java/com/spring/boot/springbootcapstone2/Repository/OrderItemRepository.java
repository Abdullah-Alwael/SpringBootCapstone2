package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<Items, Integer> {
    Items findOrderItemsById(Integer id);

    List<Items> findOrderItemsByOrderId(Integer orderId);
}
