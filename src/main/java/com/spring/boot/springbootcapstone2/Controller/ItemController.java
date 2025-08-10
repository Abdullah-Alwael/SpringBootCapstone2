package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Item;
import com.spring.boot.springbootcapstone2.Service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrderItem(@Valid @RequestBody Item orderItem, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        itemService.addOrderItem(orderItem);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("OrderItem added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Item>> getOrderItems() {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getOrderItems());
    }


    @PutMapping("/update/{orderItemId}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Integer orderItemId,
                                             @Valid @RequestBody Item orderItem, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        itemService.updateOrderItem(orderItemId, orderItem);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("OrderItem updated successfully"));

    }


    @DeleteMapping("/delete/{orderItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Integer orderItemId) {

        itemService.deleteOrderItem(orderItemId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("OrderItem deleted successfully"));

    }

    @PutMapping("/confirm/{orderId}/{farmerId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Integer orderId, @PathVariable Integer farmerId){
        itemService.confirmOrder(orderId, farmerId);

        return ResponseEntity.status(HttpStatus.OK).body(new
                ApiResponse("Order confirmed successfully and stock updated"));
    }

    @PutMapping("/cancel/{orderId}/{farmerId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, @PathVariable Integer farmerId){
        itemService.cancelOrder(orderId,farmerId);

        return ResponseEntity.status(HttpStatus.OK).body(new
                ApiResponse("Order canceled successfully and stock updated"));
    }
}
