package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

@Component
public class ItemMapper {

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), itemDto.getOwner(), itemDto.getRequest());
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getOwner(), item.getRequest());
    }

    public static ItemWithBookingDto toItemWithBookingDto(Item item) {
        return new ItemWithBookingDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getOwner(), item.getRequest());
    }
}