package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Plants;
import com.spring.boot.springbootcapstone2.Model.PlantsStock;
import com.spring.boot.springbootcapstone2.Repository.PlantsStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantsStockService {

    private final PlantsStockRepository plantStocksRepository;

    // for checking the iDs existence:
    private final FarmersService farmersService;
    private final PlantsService plantsService;

    public void addPlantStock(PlantsStock plantStock) {
        if (farmersService.doesNotExist(plantStock.getFarmerId())) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantsService.doesNotExist(plantStock.getPlantId())) {
            throw new ApiException("Error, plant does not exist");
        }

        plantStocksRepository.save(plantStock);
    }

    public List<PlantsStock> getPlantsStock() {
        return plantStocksRepository.findAll();
    }

    public void updatePlantStock(Integer plantStockId, PlantsStock plantStock) {
        if (farmersService.doesNotExist(plantStock.getFarmerId())) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantsService.doesNotExist(plantStock.getPlantId())) {
            throw new ApiException("Error, plant does not exist");
        }

        PlantsStock oldPlantStock = plantStocksRepository.findPlantsStockById(plantStockId);

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
        PlantsStock oldPlantStock = plantStocksRepository.findPlantsStockById(plantStockId);

        if (oldPlantStock == null) {
            throw new ApiException("Error, plantStock does not exist");
        }

        plantStocksRepository.delete(oldPlantStock);
    }

    // Extra #1
    public List<Plants> getAllAvailablePlants(Integer farmerId) {
        List<Plants> availablePlants = new ArrayList<>();

        for (Integer id : plantStocksRepository.giveMeAvailablePlantIds(farmerId)) {
            availablePlants.add(plantsService.getPlant(id));
        }

        return availablePlants;
    }

    // Extra #2
    public List<Plants> getAllUnavailablePlants(Integer farmerId) {
        List<Plants> availablePlants = new ArrayList<>();

        for (Integer id : plantStocksRepository.giveMeUnavailablePlantIds(farmerId)) {
            availablePlants.add(plantsService.getPlant(id));
        }

        return availablePlants;
    }

    // Extra #3
    public void increaseStock(Integer farmerId, Integer plantId, Integer stockAmount) {
        if (stockAmount <= 0) {
            throw new ApiException("Error, stockAmount must be positive");
        }

        if (farmersService.doesNotExist(farmerId)) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantsService.doesNotExist(plantId)) {
            throw new ApiException("Error, plant does not exist");
        }

        PlantsStock oldPlantStock = plantStocksRepository.findPlantsStockByFarmerIdAndPlantId(farmerId, plantId);

        if (oldPlantStock == null) {
            throw new ApiException("Error, plantStock does not exist");
        }

        oldPlantStock.setStockQuantity(oldPlantStock.getStockQuantity() + stockAmount);

        plantStocksRepository.save(oldPlantStock);
    }

    // Extra #4
    public void decreaseStock(Integer farmerId, Integer plantId, Integer stockAmount) {
        if (stockAmount <= 0) {
            throw new ApiException("Error, stockAmount must be positive");
        }

        if (farmersService.doesNotExist(farmerId)) {
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantsService.doesNotExist(plantId)) {
            throw new ApiException("Error, plant does not exist");
        }

        PlantsStock oldPlantStock = plantStocksRepository.findPlantsStockByFarmerIdAndPlantId(farmerId, plantId);

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
}
