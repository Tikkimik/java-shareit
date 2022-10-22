package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

@Component
public class ItemMapper {

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setDescription(itemDto.getDescription());
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemWithBookingDto toItemWithBookingDto(Item item) {
        return new ItemWithBookingDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getOwner(), item.getRequest());
    }
}