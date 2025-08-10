package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findOrderItemsById(Integer id);

    List<Item> findOrderItemsByOrderId(Integer orderId);
}
