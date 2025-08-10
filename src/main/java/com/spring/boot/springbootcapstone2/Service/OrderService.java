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

    // for managing items inside the order
    private final ItemService itemService;

    // for checking iDs existence:
    private final FarmerService farmerService;
    private final BuyerService buyerService;
    private final PlantService plantService;

    public void addOrder(Order order){
        if (farmerService.doesNotExist(order.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyerService.doesNotExist(order.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        // set the totalPrice to initially be 0
        order.setTotalPrice(0.0);

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

    // item's methods:
    // Extra #14
    public void addItem(Item item){
        if (doesNotExist(item.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        Order order = getOrder(item.getOrderId());

        // "^(pending|confirmed|delivered|canceled)$"
        // can not add items to orders that are not pending
        if (!order.getStatus().equals("pending")){ // "^(pending|confirmed|delivered|canceled)$"
            throw new ApiException("Error, the order is already "+order.getStatus());
        }

        itemService.addItem(item); // it will check for plantId existence

        // update order total price
        order.setTotalPrice(order.getTotalPrice()
                +plantService.getPlant(item.getPlantId()).getPrice()
                *item.getQuantity());

        orderRepository.save(order);
    }

    public List<Item> getItems(Integer orderId){
        return itemService.getItems(orderId);
    }

    // Extra #15
    public void updateItem(Integer itemId, Item item){
        if (doesNotExist(item.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        // update order total price
        Order order = getOrder(item.getOrderId());

        // remove old price
        Item oldItem = itemService.getItem(itemId);

        order.setTotalPrice(order.getTotalPrice()
                -plantService.getPlant(item.getPlantId()).getPrice()
                *oldItem.getQuantity());

        itemService.updateItem(itemId,item);

        // add the new price with new quantity
        order.setTotalPrice(order.getTotalPrice()
                +plantService.getPlant(item.getPlantId()).getPrice()
                *item.getQuantity());

    }

    // Extra 12
    public void deleteItem(Integer itemId){

        // update order total price
        Item item = itemService.getItem(itemId);
        Order order = getOrder(item.getOrderId());

        // remove old price
        order.setTotalPrice(order.getTotalPrice()
                -plantService.getPlant(item.getPlantId()).getPrice()
                *item.getQuantity());

        itemService.deleteItem(itemId);
    }

    // Extra #6
    public List<Order> getPendingOrders(Integer farmerId){
        return orderRepository.findOrdersByStatusEqualsAndFarmerId("pending", farmerId);
    }

    // Extra 7:
    public void confirmOrder(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }

        Order order = getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        if (!order.getStatus().equals("pending")){ // "^(pending|confirmed|delivered|canceled)$"
            throw new ApiException("Error, the order is already "+order.getStatus());
        }

        // check if possible to decrease all items stock
        List<Item> itemList = itemService.getItems(orderId);

        for (Item item : itemList){ // check if any stock is unavailable
            if (!plantService.stockAvailable(order.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity())){
                throw new ApiException("Error, stock unavailable for "+
                        plantService.getPlant(item.getPlantId()).getName()+
                        " stock: "+plantService.getPlant(item.getPlantId()).getStockQuantity());
            }
        }

        for (Item item : itemList){ // decrease the stock
            plantService.decreaseStock(order.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity());
        }

        order.setStatus("confirmed");

        orderRepository.save(order);

    }

    // Extra 8:
    public void cancelOrder(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }

        Order order = getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        // "^(pending|confirmed|delivered|canceled)$"
        if (order.getStatus().equals("canceled")){
            throw new ApiException("Error, the order is already canceled");
        }

        if (!order.getStatus().equals("pending")){
            throw new ApiException("Error, can not cancel an order with status other than pending");
        }

        for (Item item : itemService.getItems(orderId)){ // increase the stock
            plantService.increaseStock(order.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity());
        }

        order.setStatus("canceled");

        orderRepository.save(order);

    }

    // Extra #9:
    public void markDelivered(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }
        Order order = getOrder(orderId);

        if (!order.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        // "^(pending|confirmed|delivered|canceled)$"
        if (order.getStatus().equals("delivered")){
            throw new ApiException("Error, the order is already delivered");
        }

        if (!order.getStatus().equals("confirmed")){
            throw new ApiException("Error, the order is not yet confirmed");
        }

        order.setStatus("delivered");

        orderRepository.save(order);
    }

    // Extra #10
    public List<Order> getBuyerOrders(Integer buyerId){
        return orderRepository.findOrdersByBuyerId(buyerId);
    }

    // Extra #11
    public String getFarmerSalesSummary(Integer farmerId){
        return orderRepository.giveMeSalesSummaryByFarmerId(farmerId);
    }


}
