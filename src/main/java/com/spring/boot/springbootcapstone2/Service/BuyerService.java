package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Buyer;
import com.spring.boot.springbootcapstone2.Repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerService {
    private final BuyerRepository buyerRepository;

    public void addBuyer(Buyer buyer){
        buyerRepository.save(buyer);
    }

    public List<Buyer> getBuyers(){
        return buyerRepository.findAll();
    }

    public void updateBuyer(Integer buyerId, Buyer buyer){
        Buyer oldBuyer = buyerRepository.findBuyerById(buyerId);

        if (oldBuyer == null){
            throw new ApiException("Error, buyer does not exist");
        }

        oldBuyer.setName(buyer.getName());
        oldBuyer.setAge(buyer.getAge());
        oldBuyer.setPhone(buyer.getPhone());
        oldBuyer.setEmail(buyer.getEmail());

        buyerRepository.save(oldBuyer);
    }

    public void deleteBuyer(Integer buyerId){
        Buyer oldBuyer = buyerRepository.findBuyerById(buyerId);

        if (oldBuyer == null){
            throw new ApiException("Error, buyer does not exist");
        }

       buyerRepository.delete(oldBuyer);
    }

    public Boolean doesNotExist(Integer buyerId){
        return !buyerRepository.existsById(buyerId);
    }

}
