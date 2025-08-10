package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Farmer;
import com.spring.boot.springbootcapstone2.Repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FarmerService {
    private final FarmerRepository farmerRepository;

    public void addFarmer(Farmer farmer){
        farmerRepository.save(farmer);
    }

    public List<Farmer> getFarmers(){
        return farmerRepository.findAll();
    }

    public void updateFarmer(Integer farmerId, Farmer farmer){
        Farmer oldFarmer = farmerRepository.findFarmersById(farmerId);

        if (oldFarmer == null){
            throw new ApiException("Error, farmer does not exist");
        }

        oldFarmer.setName(farmer.getName());
        oldFarmer.setAge(farmer.getAge());
        oldFarmer.setPhone(farmer.getPhone());
        oldFarmer.setEmail(farmer.getEmail());
        oldFarmer.setFarmName(farmer.getFarmName());
        oldFarmer.setLocation(farmer.getLocation());

        farmerRepository.save(oldFarmer);
    }

    public void deleteFarmer(Integer farmerId){
        Farmer oldFarmer = farmerRepository.findFarmersById(farmerId);

        if (oldFarmer == null){
            throw new ApiException("Error, farmer does not exist");
        }

        farmerRepository.delete(oldFarmer);
    }

    public Boolean doesNotExist(Integer farmerId){
        return !farmerRepository.existsById(farmerId);
    }
}
