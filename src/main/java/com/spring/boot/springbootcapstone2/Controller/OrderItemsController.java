package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.OrderItems;
import com.spring.boot.springbootcapstone2.Service.OrderItemsService;
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
public class OrderItemsController {
    private final OrderItemsService orderItemsService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrderItem(@Valid @RequestBody OrderItems orderItem, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        orderItemsService.addOrderItem(orderItem);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("OrderItem added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<OrderItems>> getOrderItems() {
        return ResponseEntity.status(HttpStatus.OK).body(orderItemsService.getOrderItems());
    }


    @PutMapping("/update/{orderItemId}")
    public ResponseEntity<?> updateOrderItem(@PathVariable Integer orderItemId,
                                         @Valid @RequestBody OrderItems orderItem, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        orderItemsService.updateOrderItem(orderItemId, orderItem);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("OrderItem updated successfully"));

    }


    @DeleteMapping("/delete/{orderItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Integer orderItemId) {

        orderItemsService.deleteOrderItem(orderItemId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("OrderItem deleted successfully"));

    }
}
