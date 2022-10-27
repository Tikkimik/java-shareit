package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepository {

    private final Map<Long, Item> itemStorage;
    private Long itemId;

    public ItemRepository() {
        this.itemStorage = new HashMap<>();
        this.itemId = 1L;
    }

    private Long generateId() {
        return itemId++;
    }

    public Item createItem(Item item) {
        Long itemId = generateId();
        item.setId(itemId);
        itemStorage.put(itemId, item);
        return item;
    }

    public Item getItem(Long itemId) {
        return itemStorage.get(itemId);
    }

    public List<Item> getAllItems(Long userId) {
        List<Item> userItems = new ArrayList<>();

        for (Item i : itemStorage.values()) {
            if (Objects.equals(i.getOwner().getId(), userId)) {
                userItems.add(i);
            }
        }

        return userItems;
    }

    public List<Item> searchAvailableItem(String text) {
        List<Item> researchList = new ArrayList<>();
        String searchText = text.toLowerCase();

        if (searchText.equals("")) {
            return researchList;
        }

        for (Item i : itemStorage.values()) {
            if (i.getAvailable()) {
                if (i.getName().toLowerCase().contains(searchText)) {
                    researchList.add(i);
                } else {
                    if (i.getDescription().toLowerCase().contains(searchText)) {
                        researchList.add(i);
                    }
                }
            }
        }

        return researchList;
    }
}
