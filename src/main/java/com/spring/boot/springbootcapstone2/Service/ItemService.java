package com.spring.boot.springbootcapstone2.Service;

import com.spring.boot.springbootcapstone2.Api.ApiException;
import com.spring.boot.springbootcapstone2.Model.Item;
import com.spring.boot.springbootcapstone2.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    // and checking the iDs existence:
    private final PlantService plantService;

// Todo fix relationships with orderService

    public void addItem(Item item){
        if (plantService.doesNotExist(item.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        itemRepository.save(item);
    }

    public List<Item> getItems(){
        return itemRepository.findAll();
    }

    public void updateItem(Integer itemId, Item item){
        if (plantService.doesNotExist(item.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        if (orderService.doesNotExist(item.getOrderId())){
            throw new ApiException("Error, order does not exist");
        }

        Item oldItem = itemRepository.findItemById(itemId);

        if (oldItem == null){
            throw new ApiException("Error, item does not exist");
        }

        oldItem.setOrderId(item.getOrderId());
        oldItem.setPlantId(item.getPlantId());
        oldItem.setQuantity(item.getQuantity());

        itemRepository.save(oldItem);
    }

    public void deleteItem(Integer itemId){
        Item oldItem = itemRepository.findItemById(itemId);

        if (oldItem == null){
            throw new ApiException("Error, item does not exist");
        }

        itemRepository.delete(oldItem);
    }

}
