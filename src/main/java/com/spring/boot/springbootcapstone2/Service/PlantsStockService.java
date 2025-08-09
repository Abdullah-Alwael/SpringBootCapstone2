package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.PlantsStock;
import com.spring.boot.springbootcapstone2.Repository.PlantsStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantsStockService {

    private final PlantsStockRepository plantStocksRepository;

    // for checking the iDs existence:
    private final FarmersService farmersService;
    private final PlantsService plantsService;

    public void addPlantStock(PlantsStock plantStock){
        if (farmersService.doesNotExist(plantStock.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantsService.doesNotExist(plantStock.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        plantStocksRepository.save(plantStock);
    }

    public List<PlantsStock> getPlantsStock(){
        return plantStocksRepository.findAll();
    }

    public void updatePlantStock(Integer plantStockId, PlantsStock plantStock){
        if (farmersService.doesNotExist(plantStock.getFarmerId())){
            throw new ApiException("Error, farmer does not exist");
        }

        if (plantsService.doesNotExist(plantStock.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        PlantsStock oldPlantStock = plantStocksRepository.findPlantsStockById(plantStockId);

        if (oldPlantStock == null){
            throw new ApiException("Error, plantStock does not exist");
        }

        oldPlantStock.setStockQuantity(plantStock.getStockQuantity());
        oldPlantStock.setPrice(plantStock.getPrice());
        oldPlantStock.setPlantId(plantStock.getPlantId());
        oldPlantStock.setFarmerId(plantStock.getFarmerId());

        plantStocksRepository.save(oldPlantStock);
    }

    public void deletePlantStock(Integer plantStockId){
        PlantsStock oldPlantStock = plantStocksRepository.findPlantsStockById(plantStockId);

        if (oldPlantStock == null){
            throw new ApiException("Error, plantStock does not exist");
        }

        plantStocksRepository.delete(oldPlantStock);
    }
}
