package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    ItemServiceImpl itemService;

    @PostMapping
    ItemDto createItem (@RequestHeader(value="X-Sharer-User-Id") Long userId,
                        @Validated({Create.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem (@PathVariable Long itemId,
                        @RequestHeader(value="X-Sharer-User-Id") Long userId,
                        @Validated({Update.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @GetMapping
    List<ItemDto> getAllItems(@RequestHeader(value="X-Sharer-User-Id") Long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    List<ItemDto> searchAvailableItem (@RequestParam String text){
        return itemService.searchAvailableItem(text);
    }


}
