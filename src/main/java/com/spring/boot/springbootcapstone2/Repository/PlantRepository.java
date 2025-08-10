package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    Plant findPlantById(Integer id);

    @Query("select ps from Plant ps where ps.stockQuantity>0 and ps.farmerId=?1")
    List<Plant> giveMeAvailablePlants(Integer farmerId);

    @Query("select ps from Plant ps where ps.stockQuantity<=0 and ps.farmerId=?1")
    List<Plant> giveMeUnavailablePlants(Integer farmerId);

    Plant findPlantByFarmerIdAndId(Integer farmerId, Integer id);

    @Query("select ps from Plant ps where  ps.farmerId=?1 and ps.price between ?2 and ?3")
    List<Plant> giveMePlantsWithinPriceRange(Integer farmerId, Double min, Double max);
}
