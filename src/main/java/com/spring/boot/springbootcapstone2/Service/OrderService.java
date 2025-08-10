package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Order;
import com.spring.boot.springbootcapstone2.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // for checking iDs existence:
    private final FarmerService farmerService;
    private final BuyerService buyerService;

    public void addOrder(Order order){
        if (farmerService.doesNotExist(order.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyerService.doesNotExist(order.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        orderRepository.save(order);
    }

    public List<Order> getOrders(){
        return orderRepository.findAll();
    }

    public Order getOrder(Integer orderId){
        return orderRepository.findOrdersById(orderId);
    }

    public void updateOrder(Integer orderId, Order order){
        if (farmerService.doesNotExist(order.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyerService.doesNotExist(order.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        Order oldOrder = orderRepository.findOrdersById(orderId);

        if (oldOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        oldOrder.setStatus(order.getStatus());
        oldOrder.setTotalAmount(order.getTotalAmount());
        oldOrder.setDate(order.getDate());
        oldOrder.setBuyerId(order.getBuyerId());
        oldOrder.setFarmerId(order.getFarmerId());

        orderRepository.save(oldOrder);
    }

    public void deleteOrder(Integer orderId){
        Order oldOrder = orderRepository.findOrdersById(orderId);

        if (oldOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        orderRepository.delete(oldOrder);
    }

    public Boolean doesNotExist(Integer orderId){
        return !orderRepository.existsById(orderId);
    }

    // Extra #6
    public List<Order> pendingOrders(Integer farmerId){
        return orderRepository.findOrdersByStatusEqualsAndFarmerId("pending", farmerId);
    }

    public void save(Order order){
        orderRepository.save(order);
    }

    // Extra #9: "^(pending|confirmed|delivered|canceled)$"

    public void markDelivered(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }
        Order order = getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        if (!order.getStatus().equals("confirmed")){
            throw new ApiException("Error, the order is not yet confirmed");
        }

        order.setStatus("delivered");

        orderRepository.save(order);
    }
}
