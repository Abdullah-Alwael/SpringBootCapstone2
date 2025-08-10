package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Item;
import com.spring.boot.springbootcapstone2.Model.Order;
import com.spring.boot.springbootcapstone2.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // for managing items inside the order TODO fix the relationship
    private final ItemService itemService;

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
        return orderRepository.findOrderById(orderId);
    }

    public void updateOrder(Integer orderId, Order order){
        if (farmerService.doesNotExist(order.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyerService.doesNotExist(order.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        Order oldOrder = orderRepository.findOrderById(orderId);

        if (oldOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        oldOrder.setStatus(order.getStatus());
        oldOrder.setTotalPrice(order.getTotalPrice());
        oldOrder.setDate(order.getDate());
        oldOrder.setBuyerId(order.getBuyerId());
        oldOrder.setFarmerId(order.getFarmerId());

        orderRepository.save(oldOrder);
    }

    public void deleteOrder(Integer orderId){
        Order oldOrder = orderRepository.findOrderById(orderId);

        if (oldOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        orderRepository.delete(oldOrder);
    }

    // helper method
    public Boolean doesNotExist(Integer orderId){
        return !orderRepository.existsById(orderId);
    }

    // TODO item's methods:

    public void addItem(Item item){
        if (doesNotExist(item.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        itemService.addItem(item);
    }



    // Extra #6
    public List<Order> pendingOrders(Integer farmerId){
        return orderRepository.findOrdersByStatusEqualsAndFarmerId("pending", farmerId);
    }

    public void save(Order order){
        orderRepository.save(order);
    }


    // TODO move the order logic to orderService if possible
    // Extra 7: "^(pending|confirmed|delivered|canceled)$"
    public void confirmOrder(Integer orderId, Integer farmerId){
        if (orderService.doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }

        Order order = orderService.getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        if (!order.getStatus().equals("pending")){
            throw new ApiException("Error, the order is already "+order.getStatus());
        }

        // check if possible to decrease all items stock
        List<Item> itemList = itemRepository.findItemsByOrderId(orderId);

        for (Item item : itemList){ // check if any stock is unavailable
            if (!plantService.stockAvailable(order.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity())){
                throw new ApiException("Error, stock unavailable");
            }
        }

        for (Item item : itemList){ // decrease the stock
            plantService.decreaseStock(order.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity());
        }

        order.setStatus("confirmed");

        orderService.save(order);

    }
// TODO move the order logic to orderService if possible

    // Extra 8 "^(pending|confirmed|delivered|canceled)$"
    public void cancelOrder(Integer orderId, Integer farmerId){
        if (orderService.doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }

        Order order = orderService.getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        if (order.getStatus().equals("canceled")){
            throw new ApiException("Error, the order is already canceled");
        }

        if (!order.getStatus().equals("pending")){
            throw new ApiException("Error, can not cancel an order with status other than pending");
        }

        List<Item> itemList = itemRepository.findItemsByOrderId(orderId);

        for (Item item : itemList){ // increase the stock
            plantService.increaseStock(order.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity());
        }

        order.setStatus("canceled");

        orderService.save(order);

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
