package com.spring.boot.springbootcapstone2.Controller;

import com.spring.boot.springbootcapstone2.Api.ApiResponse;
import com.spring.boot.springbootcapstone2.Model.PlantsStock;
import com.spring.boot.springbootcapstone2.Service.PlantsStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plants-stock")
@RequiredArgsConstructor
public class PlantsStockController {
    private final PlantsStockService plantsStockService;

    @PostMapping("/add")
    public ResponseEntity<?> addPlantStock(@Valid @RequestBody PlantsStock plantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        plantsStockService.addPlantStock(plantStock);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("PlantStock added successfully"));
    }


    @GetMapping("/list")
    public ResponseEntity<List<PlantsStock>> getPlantsStock() {
        return ResponseEntity.status(HttpStatus.OK).body(plantsStockService.getPlantsStock());
    }


    @PutMapping("/update/{plantStockId}")
    public ResponseEntity<?> updatePlantStock(@PathVariable Integer plantStockId,
                                         @Valid @RequestBody PlantsStock plantStock, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
                    ApiResponse(errors.getFieldError().getDefaultMessage()));
        }

        plantsStockService.updatePlantStock(plantStockId, plantStock);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("PlantStock updated successfully"));

    }


    @DeleteMapping("/delete/{plantStockId}")
    public ResponseEntity<?> deletePlantStock(@PathVariable Integer plantStockId) {

        plantsStockService.deletePlantStock(plantStockId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("PlantStock deleted successfully"));

    }

    // Extra #1
    @GetMapping("filter/plants/in-stock/{farmerId}")
    public ResponseEntity<?> getInStockPlants(@PathVariable Integer farmerId){
        return ResponseEntity.status(HttpStatus.OK).body(plantsStockService.getAllAvailablePlants(farmerId));
    }

    // Extra #2
    @GetMapping("filter/plants/out-of-stock/{farmerId}")
    public ResponseEntity<?> getOutOfStockPlants(@PathVariable Integer farmerId){
        return ResponseEntity.status(HttpStatus.OK).body(plantsStockService.getAllUnavailablePlants(farmerId));
    }

    // Extra #3
    @PutMapping("/increase-stock/{farmerId}/{plantId}/{stockAmount}")
    public ResponseEntity<?> increaseStock(
            @PathVariable Integer farmerId,
            @PathVariable Integer plantId,
            @PathVariable Integer stockAmount){

        plantsStockService.increaseStock(farmerId,plantId,stockAmount);
        return ResponseEntity.status(HttpStatus.OK).body(new
                ApiResponse("Stock increased by "+stockAmount+" successfully"));
    }

    // Extra #4
    @PutMapping("/decrease-stock/{farmerId}/{plantId}/{stockAmount}")
    public ResponseEntity<?> decreaseStock(
            @PathVariable Integer farmerId,
            @PathVariable Integer plantId,
            @PathVariable Integer stockAmount){

        plantsStockService.decreaseStock(farmerId,plantId,stockAmount);
        return ResponseEntity.status(HttpStatus.OK).body(new
                ApiResponse("Stock decreased by "+stockAmount+" successfully"));
    }

    // Extra #5
    @GetMapping("/filter/price/between/{minPrice}/{maxPrice}/{farmerId}")
    public ResponseEntity<?> getPlantsWithinPriceRange(@PathVariable Integer farmerId,
                                                       @PathVariable Double minPrice,
                                                       @PathVariable Double maxPrice){

        return ResponseEntity.status(HttpStatus.OK).body(
                plantsStockService.getPlantsWithinPriceRange(farmerId,minPrice,maxPrice));
    }
}
