package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Plants;
import com.spring.boot.springbootcapstone2.Repository.PlantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantsService {

    private final PlantsRepository plantsRepository;

    public void addPlant(Plants plant){
        plantsRepository.save(plant);
    }

    public List<Plants> getPlants(){
        return plantsRepository.findAll();
    }

    public void updatePlant(Integer plantId, Plants plant){
        Plants oldPlant = plantsRepository.findPlantsById(plantId);

        if (oldPlant == null){
            throw new ApiException("Error, plant does not exist");
        }

        oldPlant.setName(plant.getName());
        oldPlant.setDescription(plant.getDescription());

        plantsRepository.save(oldPlant);
    }

    public void deletePlant(Integer plantId){
        Plants oldPlant = plantsRepository.findPlantsById(plantId);

        if (oldPlant == null){
            throw new ApiException("Error, plant does not exist");
        }

        plantsRepository.delete(oldPlant);
    }
}
