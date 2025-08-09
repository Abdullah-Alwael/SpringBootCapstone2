package com.spring.boot.springbootcapstone2.Repository;

import com.spring.boot.springbootcapstone2.Model.Buyers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyersRepository extends JpaRepository<Buyers, Integer> {
    Buyers findBuyersById(Integer id);
}
