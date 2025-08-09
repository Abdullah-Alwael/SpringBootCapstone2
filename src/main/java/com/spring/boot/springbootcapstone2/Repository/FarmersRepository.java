package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Farmers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmersRepository extends JpaRepository<Farmers, Integer> {
    Farmers findFarmersById(Integer id);
}
