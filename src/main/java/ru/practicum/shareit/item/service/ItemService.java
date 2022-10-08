package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(long userId, ItemDto itemDto) throws CreatingException, NotFoundParameterException;

    ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) throws NotFoundParameterException;

    List<ItemDto> getAllItems(Long userId);

    ItemDto getItem(Long itemId);

    List<ItemDto> searchAvailableItem(String text);
}
