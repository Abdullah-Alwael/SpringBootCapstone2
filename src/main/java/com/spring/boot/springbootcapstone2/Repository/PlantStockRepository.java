package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.PlantStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantStockRepository extends JpaRepository<PlantStock, Integer> {
    PlantStock findPlantsStockById(Integer id);

    @Query("select ps.plantId from PlantStock ps where ps.stockQuantity>0 and ps.farmerId=?1")
    List<Integer> giveMeAvailablePlantIds(Integer farmerId);

    @Query("select ps.plantId from PlantStock ps where ps.stockQuantity<=0 and ps.farmerId=?1")
    List<Integer> giveMeUnavailablePlantIds(Integer farmerId);

    PlantStock findPlantsStockByFarmerIdAndPlantId(Integer farmerId, Integer plantId);

    @Query("select ps.plantId from PlantStock ps where  ps.farmerId=?1 and ps.price between ?2 and ?3")
    List<Integer> giveMePlantIdsWithinPriceRange(Integer farmerId, Double min, Double max);
}
