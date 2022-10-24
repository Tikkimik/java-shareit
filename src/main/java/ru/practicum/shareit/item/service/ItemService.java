package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto) throws CreatingException, NotFoundParameterException;

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws NotFoundParameterException;

    List<ItemWithBookingDto> getAllByUserId(Long userId);

    ItemWithBookingDto getItem(Long userId, Long itemId) throws NotFoundParameterException;

    List<ItemDto> searchAvailableItem(String text) throws NotFoundParameterException;

    CommentWithAuthorAndItemDto addComment(Long userId, Long itemId, CommentDto commentDto) throws CreatingException;

    void delete(Long userId, Long itemId);

}
