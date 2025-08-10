package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Farmer;
import com.spring.boot.springbootcapstone2.Model.Plant;
import com.spring.boot.springbootcapstone2.Repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;

    // for checking farmer existence:
    private final FarmerService farmerService;

    public void addPlant(Plant plant) {
        if (farmerService.doesNotExist(plant.getFarmerId())) {
            throw new ApiException("Error, farmer does not exist");
        }

        plantRepository.save(plant);
    }

    public List<Plant> getPlants() {
        return plantRepository.findAll();
    }

    public Plant getPlant(Integer plantId){
        return plantRepository.findPlantById(plantId);
    }

    public void updatePlant(Integer plantId, Plant plant) {
        if (farmerService.doesNotExist(plant.getFarmerId())) {
            throw new ApiException("Error, farmer does not exist");
        }

        Plant oldPlant = getPlant(plantId);

        if (oldPlant == null) {
            throw new ApiException("Error, plant does not exist");
        }

        oldPlant.setName(plant.getName());
        oldPlant.setDescription(plant.getDescription());

        oldPlant.setStockQuantity(plant.getStockQuantity());

        // price is not changed for order history ro function correctly, if a farmer wants to increase the price,
        // they would need to create another new plant with the increased price

        oldPlant.setFarmerId(plant.getFarmerId());

        plantRepository.save(oldPlant);
    }

    public void deletePlant(Integer plantId) {
        Plant oldPlant = getPlant(plantId);

        if (oldPlant == null) {
            throw new ApiException("Error, plant does not exist");
        }

        plantRepository.delete(oldPlant);
    }


    public Boolean doesNotExist(Integer plantId){
        return plantRepository.existsById(plantId);
    }

    // Extra #1
    public List<Plant> getAllAvailablePlants(Integer farmerId) {
        return plantRepository.giveMeAvailablePlants(farmerId);
    }

    // Extra #2
    public List<Plant> getAllUnavailablePlants(Integer farmerId) {
        return plantRepository.giveMeUnavailablePlants(farmerId);
    }

    // Extra #3
    public void increaseStock(Integer farmerId, Integer plantId, Integer stockAmount) {
        if (stockAmount <= 0) {
            throw new ApiException("Error, stockAmount must be positive");
        }

        if (farmerService.doesNotExist(farmerId)) {
            throw new ApiException("Error, farmer does not exist");
        }

        // making sure the farmer is the owner of the plant:
        Plant oldPlant = plantRepository.findPlantByFarmerIdAndId(farmerId, plantId);

        if (oldPlant == null) {
            throw new ApiException("Error, plant does not exist");
        }

        oldPlant.setStockQuantity(oldPlant.getStockQuantity() + stockAmount);

        plantRepository.save(oldPlant);
    }

    // helper method
    public Boolean stockAvailable(Integer farmerId, Integer plantId, Integer quantity){
        Plant oldPlant = plantRepository.findPlantByFarmerIdAndId(farmerId, plantId);

        return oldPlant.getStockQuantity() - quantity >= 0;
    }

    // Extra #4
    public void decreaseStock(Integer farmerId, Integer plantId, Integer stockAmount) {
        if (stockAmount <= 0) {
            throw new ApiException("Error, stockAmount must be positive");
        }

        if (farmerService.doesNotExist(farmerId)) {
            throw new ApiException("Error, farmer does not exist");
        }

        Plant oldPlant = plantRepository.findPlantByFarmerIdAndId(farmerId, plantId);

        if (oldPlant == null) {
            throw new ApiException("Error, plant does not exist");
        }

        if (oldPlant.getStockQuantity() - stockAmount < 0) {
            throw new ApiException("Error, can not decrease by "+stockAmount+
                    " insufficient stock: "+ oldPlant.getStockQuantity()+" for "+oldPlant.getName());
        }

        oldPlant.setStockQuantity(oldPlant.getStockQuantity() - stockAmount);

        plantRepository.save(oldPlant);
    }

    // Extra #5
    public List<Plant> getPlantsWithinPriceRange(Integer farmerId, Double min, Double max){
        return plantRepository.giveMePlantsWithinPriceRange(farmerId,min,max);
    }

    // Extra #13
    public List<Farmer> getFarmersSellingPlant(Integer plantId){
        Plant plant = getPlant(plantId);

        if (plant == null){
            throw new ApiException("Error, plant does not exist");
        }

        return farmerService.getFarmersByIdIn(plantRepository.giveMeFarmerIdsSellingPlant(plant.getName()));
    }
}
