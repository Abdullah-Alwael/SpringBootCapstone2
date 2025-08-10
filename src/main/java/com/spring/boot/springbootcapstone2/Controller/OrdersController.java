package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Orders;
import com.spring.boot.springbootcapstone2.Service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody Orders order, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        ordersService.addOrder(order);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Orders>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.getOrders());
    }


    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderId,
                                         @Valid @RequestBody Orders order, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        ordersService.updateOrder(orderId, order);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order updated successfully"));

    }


    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer orderId) {

        ordersService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order deleted successfully"));

    }

    @GetMapping("/filter/pending/{farmerId}")
    public ResponseEntity<?> pendingOrders(@PathVariable Integer farmerId){
        return ResponseEntity.status(HttpStatus.OK).body(ordersService.pendingOrders(farmerId));
    }
}
