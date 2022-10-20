package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.helperСlasses.Create;
import ru.practicum.shareit.helperСlasses.Update;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemServiceImpl itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @Validated({Create.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) throws NotFoundParameterException {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItem(@RequestParam String text) {
        return itemService.searchAvailableItem(text);
    }
}
