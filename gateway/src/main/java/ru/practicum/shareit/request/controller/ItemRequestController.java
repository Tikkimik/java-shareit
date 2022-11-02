package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody ItemRequestDescriptionDto itemRequestDescriptionDto) throws NotFoundParameterException {
        log.info("Create new item request.");
        return itemRequestClient.add(userId, itemRequestDescriptionDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) throws NotFoundParameterException {
        log.info("Get all item request by booker from={}, size={}", from, size);
        return itemRequestClient.get(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId) throws NotFoundParameterException {
        log.info("Get item request by request id.");
        return itemRequestClient.getById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId) throws NotFoundParameterException {
        log.info("Get item request by user id.");
        return itemRequestClient.getByUser(userId);
    }
}
