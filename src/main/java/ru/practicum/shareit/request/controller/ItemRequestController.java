package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public RequestDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemRequestDescriptionDto itemRequestDto) throws NotFoundParameterException {
        log.info("Create new item request.");
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(value = "from", defaultValue = "0")
                                    @PositiveOrZero Integer from,
                                    @RequestParam(value = "size", defaultValue = "10")
                                    @Positive Integer size) throws NotFoundParameterException {
        PageRequest pages = PageRequest.of(from / size, size);
        log.info("Get all item request by booker from={}, size={}", from, size);
        return itemRequestService.get(userId, pages);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long requestId) throws NotFoundParameterException {
        log.info("Get item request by request id.");
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId) throws NotFoundParameterException {
        log.info("Get item request by user id.");
        return itemRequestService.getByUser(userId);
    }
}


