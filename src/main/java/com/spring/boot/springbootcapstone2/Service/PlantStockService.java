package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Plant;
import com.spring.boot.springbootcapstone2.Model.PlantStock;
import com.spring.boot.springbootcapstone2.Repository.PlantStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantStockService {

    private final PlantStockRepository plantStocksRepository;

    // for checking the iDs existence:
    private final FarmerService farmerService;
    private final PlantService plantService;

    public void addPlantStock(PlantStock plantStock) {
        if (farmerService.doesNotExist(plantStock.getFarmerId())) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantService.doesNotExist(plantStock.getPlantId())) {
            throw new ApiException("Error, plant does not exist");
        }

        plantStocksRepository.save(plantStock);
    }

    public List<PlantStock> getPlantsStock() {
        return plantStocksRepository.findAll();
    }

    public void updatePlantStock(Integer plantStockId, PlantStock plantStock) {
        if (farmerService.doesNotExist(plantStock.getFarmerId())) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantService.doesNotExist(plantStock.getPlantId())) {
            throw new ApiException("Error, plant does not exist");
        }

        PlantStock oldPlantStock = plantStocksRepository.findPlantsStockById(plantStockId);

        if (oldPlantStock == null) {
            throw new ApiException("Error, plantStock does not exist");
        }

        oldPlantStock.setStockQuantity(plantStock.getStockQuantity());
        oldPlantStock.setPrice(plantStock.getPrice());
        oldPlantStock.setPlantId(plantStock.getPlantId());
        oldPlantStock.setFarmerId(plantStock.getFarmerId());

        plantStocksRepository.save(oldPlantStock);
    }

    public void deletePlantStock(Integer plantStockId) {
        PlantStock oldPlantStock = plantStocksRepository.findPlantsStockById(plantStockId);

        if (oldPlantStock == null) {
            throw new ApiException("Error, plantStock does not exist");
        }

        plantStocksRepository.delete(oldPlantStock);
    }

    // Extra #1
    public List<Plant> getAllAvailablePlants(Integer farmerId) {
        List<Plant> availablePlants = new ArrayList<>();

        for (Integer id : plantStocksRepository.giveMeAvailablePlantIds(farmerId)) {
            availablePlants.add(plantService.getPlant(id));
        }

        return availablePlants;
    }

    // Extra #2
    public List<Plant> getAllUnavailablePlants(Integer farmerId) {
        List<Plant> availablePlants = new ArrayList<>();

        for (Integer id : plantStocksRepository.giveMeUnavailablePlantIds(farmerId)) {
            availablePlants.add(plantService.getPlant(id));
        }

        return availablePlants;
    }

    // Extra #3
    public void increaseStock(Integer farmerId, Integer plantId, Integer stockAmount) {
        if (stockAmount <= 0) {
            throw new ApiException("Error, stockAmount must be positive");
        }

        if (farmerService.doesNotExist(farmerId)) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantService.doesNotExist(plantId)) {
            throw new ApiException("Error, plant does not exist");
        }

        PlantStock oldPlantStock = plantStocksRepository.findPlantsStockByFarmerIdAndPlantId(farmerId, plantId);

        if (oldPlantStock == null) {
            throw new ApiException("Error, plantStock does not exist");
        }

        oldPlantStock.setStockQuantity(oldPlantStock.getStockQuantity() + stockAmount);

        plantStocksRepository.save(oldPlantStock);
    }

    // helper method
    public Boolean stockAvailable(Integer farmerId, Integer plantId, Integer quantity){
        PlantStock oldPlantStock = plantStocksRepository.findPlantsStockByFarmerIdAndPlantId(farmerId, plantId);

        return oldPlantStock.getStockQuantity() - quantity >= 0;
    }

    // Extra #4
    public void decreaseStock(Integer farmerId, Integer plantId, Integer stockAmount) {
        if (stockAmount <= 0) {
            throw new ApiException("Error, stockAmount must be positive");
        }

        if (farmerService.doesNotExist(farmerId)) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantService.doesNotExist(plantId)) {
            throw new ApiException("Error, plant does not exist");
        }

        PlantStock oldPlantStock = plantStocksRepository.findPlantsStockByFarmerIdAndPlantId(farmerId, plantId);

        if (oldPlantStock == null) {
            throw new ApiException("Error, plantStock does not exist");
        }

        if (oldPlantStock.getStockQuantity() - stockAmount < 0) {
            throw new ApiException("Error, can not decrease by "+stockAmount+
                    " insufficient stock: "+oldPlantStock.getStockQuantity());
        }

        oldPlantStock.setStockQuantity(oldPlantStock.getStockQuantity() - stockAmount);

        plantStocksRepository.save(oldPlantStock);
    }

    // Extra #5

    public List<Plant> getPlantsWithinPriceRange(Integer farmerId, Double min, Double max){
        List<Plant> plantsWithinRange = new ArrayList<>();

        for (Integer id :plantStocksRepository.giveMePlantIdsWithinPriceRange(farmerId,min,max)) {
            plantsWithinRange.add(plantService.getPlant(id));
        }

        return plantsWithinRange;

    }
}
