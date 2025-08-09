package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.OrderItems;
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
}
