package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public RequestDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemRequestDescriptionDto itemRequestDto) {
        return itemRequestService.add(userId, itemRequestDto);
    }


}
