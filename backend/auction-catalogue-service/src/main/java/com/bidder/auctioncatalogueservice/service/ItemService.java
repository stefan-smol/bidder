package com.bidder.auctioncatalogueservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidder.auctioncatalogueservice.model.Item;
import com.bidder.auctioncatalogueservice.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for this id: " + id));
        item.setName(itemDetails.getName());
        item.setDescription(itemDetails.getDescription());
        item.setMsrp(itemDetails.getMsrp());
        item.setCategory(itemDetails.getCategory());
        item.setBrand(itemDetails.getBrand());
        item.setItemCondition(itemDetails.getItemCondition());
        final Item updatedItem = itemRepository.save(item);
        
        return updatedItem;
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for this id: " + id));
        itemRepository.delete(item);
    }
}

