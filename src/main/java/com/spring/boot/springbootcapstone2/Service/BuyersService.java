package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Buyers;
import com.spring.boot.springbootcapstone2.Repository.BuyersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyersService {
    private final BuyersRepository buyersRepository;

    public void addBuyer(Buyers buyer){
        buyersRepository.save(buyer);
    }

    public List<Buyers> getBuyers(){
        return buyersRepository.findAll();
    }

    public void updateBuyer(Integer buyerId, Buyers buyer){
        Buyers oldBuyer = buyersRepository.findBuyersById(buyerId);

        if (oldBuyer == null){
            throw new ApiException("Error, buyer does not exist");
        }

        oldBuyer.setName(buyer.getName());
        oldBuyer.setAge(buyer.getAge());
        oldBuyer.setPhone(buyer.getPhone());
        oldBuyer.setEmail(buyer.getEmail());

        buyersRepository.save(oldBuyer);
    }

    public void deleteBuyer(Integer buyerId){
        Buyers oldBuyer = buyersRepository.findBuyersById(buyerId);

        if (oldBuyer == null){
            throw new ApiException("Error, buyer does not exist");
        }

       buyersRepository.delete(oldBuyer);
    }


}
