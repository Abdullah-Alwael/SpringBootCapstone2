package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Orders;
import com.spring.boot.springbootcapstone2.Repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    // for checking iDs existence:
    private final FarmersService farmersService;
    private final BuyersService buyersService;

    public void addOrder(Orders order){
        if (farmersService.doesNotExist(order.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyersService.doesNotExist(order.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        ordersRepository.save(order);
    }

    public List<Orders> getOrders(){
        return ordersRepository.findAll();
    }

    public void updateOrder(Integer orderId, Orders order){
        if (farmersService.doesNotExist(order.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (buyersService.doesNotExist(order.getBuyerId())){
            throw new ApiException("Error, buyer does not exist");
        }

        Orders oldOrder = ordersRepository.findOrdersById(orderId);

        if (oldOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        oldOrder.setStatus(order.getStatus());
        oldOrder.setTotalAmount(order.getTotalAmount());
        oldOrder.setDate(order.getDate());
        oldOrder.setBuyerId(order.getBuyerId());
        oldOrder.setFarmerId(order.getFarmerId());

        ordersRepository.save(oldOrder);
    }

    public void deleteOrder(Integer orderId){
        Orders oldOrder = ordersRepository.findOrdersById(orderId);

        if (oldOrder == null){
            throw new ApiException("Error, order does not exist");
        }

        ordersRepository.delete(oldOrder);
    }

    public Boolean doesNotExist(Integer orderId){
        return !ordersRepository.existsById(orderId);
    }
}
