package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.OrderItems;
import com.spring.boot.springbootcapstone2.Model.Orders;
import com.spring.boot.springbootcapstone2.Repository.OrderItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemsService {
    private final OrderItemsRepository orderItemsRepository;

    // for checking the iDs existence:
    private final PlantsService plantsService;
    private final OrdersService ordersService;

    // for manipulating the stock
    private final PlantsStockService plantsStockService;

    public void addOrderItem(OrderItems orderItem){
        if (plantsService.doesNotExist(orderItem.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (ordersService.doesNotExist(orderItem.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }


        orderItemsRepository.save(orderItem);
    }

    public List<OrderItems> getOrderItems(){
        return orderItemsRepository.findAll();
    }

    public void updateOrderItem(Integer orderItemId, OrderItems orderItem){
        if (plantsService.doesNotExist(orderItem.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (ordersService.doesNotExist(orderItem.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        OrderItems oldOrderItem = orderItemsRepository.findOrderItemsById(orderItemId);

        if (oldOrderItem == null){
            throw new ApiException("Error, orderItem does not exist");
        }

        oldOrderItem.setOrderId(orderItem.getOrderId());
        oldOrderItem.setPlantId(orderItem.getPlantId());
        oldOrderItem.setQuantity(orderItem.getQuantity());
        oldOrderItem.setPurchasePrice(orderItem.getPurchasePrice());

        orderItemsRepository.save(oldOrderItem);
    }

    public void deleteOrderItem(Integer orderItemId){
        OrderItems oldOrderItem = orderItemsRepository.findOrderItemsById(orderItemId);

        if (oldOrderItem == null){
            throw new ApiException("Error, orderItem does not exist");
        }

        orderItemsRepository.delete(oldOrderItem);
    }

    // Extra 7
    public void confirmOrder(Integer orderId, Integer farmerId){
        if (ordersService.doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }
        Orders order = ordersService.getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        if (!order.getStatus().equals("pending")){
            throw new ApiException("Error, the order is already "+order.getStatus());
        }

        // check if possible to decrease all items stock
        List<OrderItems> orderItemsList = orderItemsRepository.findOrderItemsByOrderId(orderId);

        for (OrderItems orderItem :orderItemsList){ // check if any stock is unavailable
            if (!plantsStockService.stockAvailable(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity())){
                throw new ApiException("Error, stock unavailable");
            }
        }

        for (OrderItems orderItem :orderItemsList){ // decrease the stock
            plantsStockService.decreaseStock(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity());
        }

        order.setStatus("confirmed");

        ordersService.save(order);

    }

    // Extra 8
    public void cancelOrder(Integer orderId, Integer farmerId){
        if (ordersService.doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }
        Orders order = ordersService.getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        List<OrderItems> orderItemsList = orderItemsRepository.findOrderItemsByOrderId(orderId);

        for (OrderItems orderItem :orderItemsList){ // increase the stock
            plantsStockService.increaseStock(order.getFarmerId(),
                    orderItem.getPlantId(),
                    orderItem.getQuantity());
        }

        order.setStatus("canceled");

        ordersService.save(order);

    }
}
