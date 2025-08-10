package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.Plant;
import com.spring.boot.springbootcapstone2.Service.PlantService;
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
public class PlantController {
    private final PlantService plantService;

    @PostMapping("/add")
    public ResponseEntity<?> addPlant(@Valid @RequestBody Plant plant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        plantService.addPlant(plant);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Plant added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<Plant>> getPlants() {
        return ResponseEntity.status(HttpStatus.OK).body(plantService.getPlants());
    }


    @PutMapping("/update/{plantId}")
    public ResponseEntity<?> updatePlant(@PathVariable Integer plantId,
                                         @Valid @RequestBody Plant plant, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        plantService.updatePlant(plantId, plant);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Plant updated successfully"));

    }


    @DeleteMapping("/delete/{plantId}")
    public ResponseEntity<?> deletePlant(@PathVariable Integer plantId) {

        plantService.deletePlant(plantId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Plant deleted successfully"));

    }
}
