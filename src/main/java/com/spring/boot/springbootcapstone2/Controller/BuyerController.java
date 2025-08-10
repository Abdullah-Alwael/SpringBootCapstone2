package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Buyer;
import com.spring.boot.springbootcapstone2.Service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyer")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerService buyerService;

    @PostMapping("/add")
    public ResponseEntity<?> addBuyer(@Valid @RequestBody Buyer buyer, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        buyerService.addBuyer(buyer);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Buyer added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Buyer>> getBuyers() {
        return ResponseEntity.status(HttpStatus.OK).body(buyerService.getBuyers());
    }


    @PutMapping("/update/{buyerId}")
    public ResponseEntity<?> updateBuyer(@PathVariable Integer buyerId,
                                         @Valid @RequestBody Buyer buyer, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        buyerService.updateBuyer(buyerId, buyer);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Buyer updated successfully"));

    }


    @DeleteMapping("/delete/{buyerId}")
    public ResponseEntity<?> deleteBuyer(@PathVariable Integer buyerId) {

        buyerService.deleteBuyer(buyerId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Buyer deleted successfully"));

    }

}
