package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static Item toItem(User owner, ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), owner);
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getOwner());
    }


    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwner());
    }

    public static List<ItemDto> toListItemDto(List<Item> allItems) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(Item i: allItems) {
            itemDtoList.add(new ItemDto(i.getId(), i.getName(), i.getDescription(), i.getAvailable(), i.getOwner()));
        }

        return itemDtoList;
    }
}
