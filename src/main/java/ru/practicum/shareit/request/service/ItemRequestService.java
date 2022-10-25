package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.RequestDto;

public interface ItemRequestService {

    RequestDto add(Long userId, ItemRequestDescriptionDto itemRequestDto);


}
