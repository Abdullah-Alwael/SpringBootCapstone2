package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Item;
import com.spring.boot.springbootcapstone2.Model.FarmOrder;
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

    public void addOrder(FarmOrder farmOrder){
        if (farmerService.doesNotExist(farmOrder.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyerService.doesNotExist(farmOrder.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        // set the totalPrice to initially be 0
        farmOrder.setTotalPrice(0.0);
        // "^(pending|confirmed|delivered|canceled)$"
        farmOrder.setStatus("pending");

        orderRepository.save(farmOrder);
    }

    public List<FarmOrder> getOrders(){
        return orderRepository.findAll();
    }

    public FarmOrder getOrder(Integer orderId){
        return orderRepository.findOrderById(orderId);
    }

    public void updateOrder(Integer orderId, FarmOrder farmOrder){
        if (farmerService.doesNotExist(farmOrder.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyerService.doesNotExist(farmOrder.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        FarmOrder oldFarmOrder = orderRepository.findOrderById(orderId);

        if (oldFarmOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        oldFarmOrder.setStatus(farmOrder.getStatus());
        oldFarmOrder.setTotalPrice(farmOrder.getTotalPrice());
        oldFarmOrder.setDate(farmOrder.getDate());
        oldFarmOrder.setBuyerId(farmOrder.getBuyerId());
        oldFarmOrder.setFarmerId(farmOrder.getFarmerId());

        orderRepository.save(oldFarmOrder);
    }

    public void deleteOrder(Integer orderId){
        FarmOrder oldFarmOrder = orderRepository.findOrderById(orderId);

        if (oldFarmOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        orderRepository.delete(oldFarmOrder);
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

        FarmOrder farmOrder = getOrder(item.getOrderId());

        // "^(pending|confirmed|delivered|canceled)$"
        // can not add items to orders that are not pending
        if (!farmOrder.getStatus().equals("pending")){ // "^(pending|confirmed|delivered|canceled)$"
            throw new ApiException("Error, the order is already "+ farmOrder.getStatus());
        }

        itemService.addItem(item); // it will check for plantId existence

        // update order total price
        farmOrder.setTotalPrice(farmOrder.getTotalPrice()
                +plantService.getPlant(item.getPlantId()).getPrice()
                *item.getQuantity());

        orderRepository.save(farmOrder);
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
        FarmOrder farmOrder = getOrder(item.getOrderId());

        // remove old price
        Item oldItem = itemService.getItem(itemId);

        farmOrder.setTotalPrice(farmOrder.getTotalPrice()
                -plantService.getPlant(item.getPlantId()).getPrice()
                *oldItem.getQuantity());

        itemService.updateItem(itemId,item);

        // add the new price with new quantity
        farmOrder.setTotalPrice(farmOrder.getTotalPrice()
                +plantService.getPlant(item.getPlantId()).getPrice()
                *item.getQuantity());

    }

    // Extra 12
    public void deleteItem(Integer itemId){

        // update order total price
        Item item = itemService.getItem(itemId);
        FarmOrder farmOrder = getOrder(item.getOrderId());

        // remove old price
        farmOrder.setTotalPrice(farmOrder.getTotalPrice()
                -plantService.getPlant(item.getPlantId()).getPrice()
                *item.getQuantity());

        itemService.deleteItem(itemId);
    }

    // Extra #6
    public List<FarmOrder> getPendingOrders(Integer farmerId){
        return orderRepository.findOrdersByStatusEqualsAndFarmerId("pending", farmerId);
    }

    // Extra 7:
    public void confirmOrder(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }

        FarmOrder farmOrder = getOrder(orderId);

        if (!farmOrder.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        if (!farmOrder.getStatus().equals("pending")){ // "^(pending|confirmed|delivered|canceled)$"
            throw new ApiException("Error, the order is already "+ farmOrder.getStatus());
        }

        // check if possible to decrease all items stock
        List<Item> itemList = itemService.getItems(orderId);

        for (Item item : itemList){ // check if any stock is unavailable
            if (!plantService.stockAvailable(farmOrder.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity())){
                throw new ApiException("Error, stock unavailable for "+
                        plantService.getPlant(item.getPlantId()).getName()+
                        " stock: "+plantService.getPlant(item.getPlantId()).getStockQuantity());
            }
        }

        for (Item item : itemList){ // decrease the stock
            plantService.decreaseStock(farmOrder.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity());
        }

        farmOrder.setStatus("confirmed");

        orderRepository.save(farmOrder);

    }

    // Extra 8:
    public void cancelOrder(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }

        FarmOrder farmOrder = getOrder(orderId);

        if (!farmOrder.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        // "^(pending|confirmed|delivered|canceled)$"
        if (farmOrder.getStatus().equals("canceled")){
            throw new ApiException("Error, the order is already canceled");
        }

        if (!farmOrder.getStatus().equals("pending")){
            throw new ApiException("Error, can not cancel an order with status other than pending");
        }

        for (Item item : itemService.getItems(orderId)){ // increase the stock
            plantService.increaseStock(farmOrder.getFarmerId(),
                    item.getPlantId(),
                    item.getQuantity());
        }

        farmOrder.setStatus("canceled");

        orderRepository.save(farmOrder);

    }

    // Extra #9:
    public void markDelivered(Integer orderId, Integer farmerId){
        if (doesNotExist(orderId)){
            throw new ApiException("Error, order does not exist");
        }
        FarmOrder farmOrder = getOrder(orderId);

        if (!farmOrder.getFarmerId().equals(farmerId)){
            throw new ApiException("Error, the order is not owned by the farmer specified");
        }

        // "^(pending|confirmed|delivered|canceled)$"
        if (farmOrder.getStatus().equals("delivered")){
            throw new ApiException("Error, the order is already delivered");
        }

        if (!farmOrder.getStatus().equals("confirmed")){
            throw new ApiException("Error, the order is not yet confirmed");
        }

        farmOrder.setStatus("delivered");

        orderRepository.save(farmOrder);
    }

    // Extra #10
    public List<FarmOrder> getBuyerOrders(Integer buyerId){
        return orderRepository.findOrdersByBuyerId(buyerId);
    }

    // Extra #11
    public String getFarmerSalesSummary(Integer farmerId){
        return orderRepository.giveMeSalesSummaryByFarmerId(farmerId);
    }


}
