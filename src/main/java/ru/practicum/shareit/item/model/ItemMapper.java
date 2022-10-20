package ru.practicum.shareit.item.model;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
@NoArgsConstructor
public class ItemMapper {

    public Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getRequest(), itemDto.getOwner());
    }

//    private Long id;
//    private String name;
//    private String description;
//    private Boolean available;
//    private String request;
//    private Long owner;

//    public static Item toItem(ItemDto itemDto) {
//        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getOwner());
//    }
//
//
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequest(), item.getOwner());
    }
}