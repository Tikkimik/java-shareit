package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface ItemRequestService {

    RequestDto add(Long userId, ItemRequestDescriptionDto itemRequestDto) throws NotFoundParameterException;

    List<ItemRequestDto> get(Long userId, PageRequest pages) throws NotFoundParameterException;

    ItemRequestDto getById(Long userId, Long requestId) throws NotFoundParameterException;

    List<ItemRequestDto> getByUser(Long userId) throws NotFoundParameterException;
}
