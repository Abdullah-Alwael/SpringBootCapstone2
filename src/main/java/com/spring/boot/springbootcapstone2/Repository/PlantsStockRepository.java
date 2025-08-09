package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.PlantsStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantsStockRepository extends JpaRepository<PlantsStock, Integer> {
    PlantsStock findPlantsStockById(Integer id);
}
