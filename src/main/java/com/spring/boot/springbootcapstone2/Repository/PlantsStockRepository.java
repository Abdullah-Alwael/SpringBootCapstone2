package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.PlantsStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantsStockRepository extends JpaRepository<PlantsStock, Integer> {
    PlantsStock findPlantsStockById(Integer id);

    @Query("select ps.plantId from PlantsStock ps where ps.stockQuantity>0 and ps.farmerId=?1")
    List<Integer> giveMeAvailablePlantIds(Integer farmerId);

    @Query("select ps.plantId from PlantsStock ps where ps.stockQuantity<=0 and ps.farmerId=?1")
    List<Integer> giveMeUnavailablePlantIds(Integer farmerId);

    PlantsStock findPlantsStockByFarmerIdAndPlantId(Integer farmerId, Integer plantId);
}
