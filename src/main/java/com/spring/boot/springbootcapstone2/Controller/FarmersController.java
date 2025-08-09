package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Farmers;
import com.spring.boot.springbootcapstone2.Service.FarmersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farmer")
@RequiredArgsConstructor
public class FarmersController {
    private final FarmersService farmersService;

    @PostMapping("/add")
    public ResponseEntity<?> addFarmer(@Valid @RequestBody Farmers farmer, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        farmersService.addFarmer(farmer);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Farmer added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Farmers>> getFarmers() {
        return ResponseEntity.status(HttpStatus.OK).body(farmersService.getFarmers());
    }


    @PutMapping("/update/{farmerId}")
    public ResponseEntity<?> updateFarmer(@PathVariable Integer farmerId,
                                          @Valid @RequestBody Farmers farmer, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        farmersService.updateFarmer(farmerId, farmer);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Farmer updated successfully"));

    }


    @DeleteMapping("/delete/{farmerId}")
    public ResponseEntity<?> deleteFarmer(@PathVariable Integer farmerId) {

        farmersService.deleteFarmer(farmerId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Farmer deleted successfully"));

    }
}
