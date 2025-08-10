package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Item;
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
    // TODO fix the relationship

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

    // Extra #6
    @GetMapping("/filter/pending/{farmerId}")
    public ResponseEntity<?> pendingOrders(@PathVariable Integer farmerId){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.pendingOrders(farmerId));
    }

    // TODO the following was moved from itemController, fix the relationships

    @PostMapping("/add/item")
    public ResponseEntity<?> addItem(@Valid @RequestBody Item item, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        itemService.addItem(item);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item added successfully"));
    }


    @GetMapping("/list/items")
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItems());
    }


    @PutMapping("/update/item/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Integer itemId,
                                        @Valid @RequestBody Item item, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        itemService.updateItem(itemId, item);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item updated successfully"));

    }


    @DeleteMapping("/delete/item/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer itemId) {

        itemService.deleteItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Item deleted successfully"));

    }

    // TODO move the order logic to orderService if possible
    // Extra #7
    @PutMapping("/confirm/{orderId}/{farmerId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Integer orderId, @PathVariable Integer farmerId){
        itemService.confirmOrder(orderId, farmerId);

        return ResponseEntity.status(HttpStatus.OK).body(new
                ApiResponse("Order confirmed successfully and stock updated"));
    }

    // TODO move the order logic to orderService if possible
    // Extra #8
    @PutMapping("/cancel/{orderId}/{farmerId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId, @PathVariable Integer farmerId){
        itemService.cancelOrder(orderId,farmerId);

        return ResponseEntity.status(HttpStatus.OK).body(new
                ApiResponse("Order canceled successfully and stock updated"));
    }
}
