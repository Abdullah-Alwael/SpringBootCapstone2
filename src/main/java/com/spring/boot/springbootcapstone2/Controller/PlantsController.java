package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Plants;
import com.spring.boot.springbootcapstone2.Service.PlantsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plant")
@RequiredArgsConstructor
public class PlantsController {
    private final PlantsService plantsService;

    @PostMapping("/add")
    public ResponseEntity<?> addPlant(@Valid @RequestBody Plants plant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        plantsService.addPlant(plant);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Plant added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Plants>> getPlants() {
        return ResponseEntity.status(HttpStatus.OK).body(plantsService.getPlants());
    }


    @PutMapping("/update/{plantId}")
    public ResponseEntity<?> updatePlant(@PathVariable Integer plantId,
                                         @Valid @RequestBody Plants plant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        plantsService.updatePlant(plantId, plant);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Plant updated successfully"));

    }


    @DeleteMapping("/delete/{plantId}")
    public ResponseEntity<?> deletePlant(@PathVariable Integer plantId) {

        plantsService.deletePlant(plantId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Plant deleted successfully"));

    }
}
