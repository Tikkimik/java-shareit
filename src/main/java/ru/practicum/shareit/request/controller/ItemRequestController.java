package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
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
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public RequestDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemRequestDescriptionDto itemRequestDto) throws NotFoundParameterException {
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(value = "from", defaultValue = "0")
                                    @PositiveOrZero Integer from,
                                    @RequestParam(value = "size", defaultValue = "10")
                                    @Positive Integer size) throws NotFoundParameterException {
        PageRequest pages = PageRequest.of(from / size, size);
        return itemRequestService.get(userId, pages);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long requestId) throws NotFoundParameterException {
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId) throws NotFoundParameterException {
        return itemRequestService.getByUser(userId);
    }
}


