package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Order;
import com.spring.boot.springbootcapstone2.Service.OrderService;
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
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody Order order, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        orderService.addOrder(order);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders());
    }


    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer orderId,
                                         @Valid @RequestBody Order order, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        orderService.updateOrder(orderId, order);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order updated successfully"));

    }


    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer orderId) {

        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order deleted successfully"));

    }

    @GetMapping("/filter/pending/{farmerId}")
    public ResponseEntity<?> pendingOrders(@PathVariable Integer farmerId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.pendingOrders(farmerId));
    }
}
