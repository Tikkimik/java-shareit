package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.helpers.Create;
import ru.practicum.shareit.helpers.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @Validated({Create.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException, CreatingException {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemWithBookingDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) throws NotFoundParameterException {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItem(@RequestParam String text) throws NotFoundParameterException {
        return itemService.searchAvailableItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentWithAuthorAndItemDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                                  @RequestBody CommentDto commentDto) throws CreatingException {
        return itemService.addComment(userId, itemId, commentDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
    }
}
