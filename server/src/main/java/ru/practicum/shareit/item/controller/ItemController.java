package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto) throws NotFoundParameterException, CreatingException {
        log.info("Create new item.");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        log.info("Update being item.");
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemWithBookingDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items by user id.");
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) throws NotFoundParameterException {
        log.info("Get item by user id.");
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItem(@RequestParam(name = "text", required = false) String text,
                                             @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) throws NotFoundParameterException {
        log.info("get all items by text query.");
        Pageable pages = PageRequest.of(from / size, size);
        return itemService.searchAvailableItem(text, pages);
    }

    @PostMapping("/{itemId}/comment")
    public CommentWithAuthorAndItemDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                                  @RequestBody CommentDto commentDto) throws CreatingException {
        log.info("Post new comment.");
        return itemService.addComment(userId, itemId, commentDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Delete comment.");
        itemService.delete(userId, itemId);
    }
}
