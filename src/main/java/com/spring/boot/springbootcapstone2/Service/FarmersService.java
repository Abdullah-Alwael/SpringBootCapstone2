package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Farmers;
import com.spring.boot.springbootcapstone2.Repository.FarmersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmersService {
    private final FarmersRepository farmersRepository;

    public void addFarmer(Farmers farmer){
        farmersRepository.save(farmer);
    }

    public List<Farmers> getFarmers(){
        return farmersRepository.findAll();
    }

    public void updateFarmer(Integer farmerId, Farmers farmer){
        Farmers oldFarmer = farmersRepository.findFarmersById(farmerId);

        if (oldFarmer == null){
            throw new ApiException("Error, farmer does not exist");
        }

        oldFarmer.setName(farmer.getName());
        oldFarmer.setAge(farmer.getAge());
        oldFarmer.setPhone(farmer.getPhone());
        oldFarmer.setEmail(farmer.getEmail());
        oldFarmer.setFarmName(farmer.getFarmName());
        oldFarmer.setLocation(farmer.getLocation());

        farmersRepository.save(oldFarmer);
    }

    public void deleteFarmer(Integer farmerId){
        Farmers oldFarmer = farmersRepository.findFarmersById(farmerId);

        if (oldFarmer == null){
            throw new ApiException("Error, farmer does not exist");
        }

        farmersRepository.delete(oldFarmer);
    }

    public Boolean doesNotExist(Integer farmerId){
        return !farmersRepository.existsById(farmerId);
    }
}
