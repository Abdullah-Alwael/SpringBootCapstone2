package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Items;
import com.spring.boot.springbootcapstone2.Model.Order;
import com.spring.boot.springbootcapstone2.Repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    // for checking the iDs existence:
    private final PlantService plantService;
    private final OrderService orderService;

    // for manipulating the stock
    private final PlantStockService plantStockService;

    public void addOrderItem(Items orderItem){
        if (plantService.doesNotExist(orderItem.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (orderService.doesNotExist(orderItem.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }


        orderItemRepository.save(orderItem);
    }

    public List<Items> getOrderItems(){
        return orderItemRepository.findAll();
    }

    public void updateOrderItem(Integer orderItemId, Items orderItem){
        if (plantService.doesNotExist(orderItem.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (orderService.doesNotExist(orderItem.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        Items oldOrderItem = orderItemRepository.findOrderItemsById(orderItemId);

        if (oldOrderItem == null){
            throw new ApiException("Error, orderItem does not exist");
        }

        oldOrderItem.setOrderId(orderItem.getOrderId());
        oldOrderItem.setPlantId(orderItem.getPlantId());
        oldOrderItem.setQuantity(orderItem.getQuantity());
        oldOrderItem.setPurchasePrice(orderItem.getPurchasePrice());

        orderItemRepository.save(oldOrderItem);
    }

    public void deleteOrderItem(Integer orderItemId){
        Items oldOrderItem = orderItemRepository.findOrderItemsById(orderItemId);

        if (oldOrderItem == null){
            throw new ApiException("Error, orderItem does not exist");
        }

        orderItemRepository.delete(oldOrderItem);
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
        List<Items> itemsList = orderItemRepository.findOrderItemsByOrderId(orderId);

        for (Items orderItem : itemsList){ // check if any stock is unavailable
            if (!plantStockService.stockAvailable(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity())){
                throw new ApiException("Error, stock unavailable");
            }
        }

        for (Items orderItem : itemsList){ // decrease the stock
            plantStockService.decreaseStock(order.getFarmerId(),
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

        List<Items> itemsList = orderItemRepository.findOrderItemsByOrderId(orderId);

        for (Items orderItem : itemsList){ // increase the stock
            plantStockService.increaseStock(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity());
        }

        order.setStatus("canceled");

        orderService.save(order);

    }
}
