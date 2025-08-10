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

    // for checking the iDs existence:
    private final PlantService plantService;

    public void addItem(Item item){
        if (plantService.doesNotExist(item.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        itemRepository.save(item);
    }

    // get only the items related to an orderId
    public List<Item> getItems(Integer orderId){
        return itemRepository.findItemsByOrderId(orderId);
    }

    public void updateItem(Integer itemId, Item item){
        if (plantService.doesNotExist(item.getPlantId())){
            throw new ApiException("Error, plant does not exist");
        }

        Item oldItem = itemRepository.findItemById(itemId);

        if (oldItem == null){
            throw new ApiException("Error, item does not exist");
        }

        // only allow to change the quantity, an item should not move to other users' orders
        // nor change plantId, if needed, delete and create a new item
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
