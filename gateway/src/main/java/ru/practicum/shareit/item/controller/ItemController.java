package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.helpers.Create;
import ru.practicum.shareit.helpers.Update;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @Validated({Create.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        log.info("Create new item.");
        return client.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @Validated({Update.class}) @RequestBody ItemDto itemDto) throws NotFoundParameterException {
        log.info("Update being item.");
        return client.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items by user id.");
        return client.getAllByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) throws NotFoundParameterException {
        log.info("Get item by user id.");
        return client.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(name = "text", required = false) String text,
                                                      @PositiveOrZero
                                                      @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                      @Positive
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {

        log.info("Get search all items by text query from={}, size={}.", from, size);
        return client.searchAvailableItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                             @RequestBody CommentDto commentDto) {
        log.info("Post new comment.");
        return client.addComment(userId, itemId, commentDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Delete comment.");
        client.delete(userId, itemId);
    }

}
