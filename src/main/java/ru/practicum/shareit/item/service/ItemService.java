package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoWithAuthorAndItem;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto) throws CreatingException, NotFoundParameterException;

    ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) throws NotFoundParameterException;

    List<ItemDto> getAllItems(Long userId);

    ItemDto getItem(Long userId, Long itemId) throws NotFoundParameterException;

    List<ItemDto> searchAvailableItem(String text);

    CommentDtoWithAuthorAndItem addComment(Long userId, Long itemId, CommentDto commentDto) throws CreatingException;

}
