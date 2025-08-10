package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Plant;
import com.spring.boot.springbootcapstone2.Repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;

    public void addPlant(Plant plant){
        plantRepository.save(plant);
    }

    public List<Plant> getPlants(){
        return plantRepository.findAll();
    }

    public Plant getPlant(Integer plantId){
        return plantRepository.findPlantsById(plantId);
    }

    public void updatePlant(Integer plantId, Plant plant){
        Plant oldPlant = plantRepository.findPlantsById(plantId);

        if (oldPlant == null){
            throw new ApiException("Error, plant does not exist");
        }

        oldPlant.setName(plant.getName());
        oldPlant.setDescription(plant.getDescription());

        plantRepository.save(oldPlant);
    }

    public void deletePlant(Integer plantId){
        Plant oldPlant = plantRepository.findPlantsById(plantId);

        if (oldPlant == null){
            throw new ApiException("Error, plant does not exist");
        }

        plantRepository.delete(oldPlant);
    }

    public Boolean doesNotExist(Integer plantId){
        return !plantRepository.existsById(plantId);
    }
}
