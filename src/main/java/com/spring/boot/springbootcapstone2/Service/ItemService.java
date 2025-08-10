package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Item;
import com.spring.boot.springbootcapstone2.Model.Order;
import com.spring.boot.springbootcapstone2.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    // for manipulating the stock
    // and checking the iDs existence:
    private final PlantService plantService;
    private final OrderService orderService;

    public void addOrderItem(Item orderItem){
        if (plantService.doesNotExist(orderItem.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (orderService.doesNotExist(orderItem.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }


        itemRepository.save(orderItem);
    }

    public List<Item> getOrderItems(){
        return itemRepository.findAll();
    }

    public void updateOrderItem(Integer orderItemId, Item orderItem){
        if (plantService.doesNotExist(orderItem.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (orderService.doesNotExist(orderItem.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        Item oldOrderItem = itemRepository.findOrderItemsById(orderItemId);

        if (oldOrderItem == null){
            throw new ApiException("Error, orderItem does not exist");
        }

        oldOrderItem.setOrderId(orderItem.getOrderId());
        oldOrderItem.setPlantId(orderItem.getPlantId());
        oldOrderItem.setQuantity(orderItem.getQuantity());

        itemRepository.save(oldOrderItem);
    }

    public void deleteOrderItem(Integer orderItemId){
        Item oldOrderItem = itemRepository.findOrderItemsById(orderItemId);

        if (oldOrderItem == null){
            throw new ApiException("Error, orderItem does not exist");
        }

        itemRepository.delete(oldOrderItem);
    }

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
        List<Item> itemList = itemRepository.findOrderItemsByOrderId(orderId);

        for (Item orderItem : itemList){ // check if any stock is unavailable
            if (!plantService.stockAvailable(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity())){
                throw new ApiException("Error, stock unavailable");
            }
        }

        for (Item orderItem : itemList){ // decrease the stock
            plantService.decreaseStock(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity());
        }

        order.setStatus("confirmed");

        orderService.save(order);

    }

    // Extra 8 "^(pending|confirmed|delivered|canceled)$"
    public void cancelOrder(Integer orderId, Integer farmerId){
        if (orderService.doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }
        Order order = orderService.getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        List<Item> itemList = itemRepository.findOrderItemsByOrderId(orderId);

        for (Item orderItem : itemList){ // increase the stock
            plantService.increaseStock(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity());
        }

        order.setStatus("canceled");

        orderService.save(order);

    }
}
