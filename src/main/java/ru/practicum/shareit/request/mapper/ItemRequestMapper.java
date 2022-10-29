package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDescriptionDto itemRequestDescriptionDto, User requestor) {
        ItemRequest request = new ItemRequest();
        request.setDescription(itemRequestDescriptionDto.getDescription());
        request.setRequester(requestor);
        return request;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(request.getId(),
                request.getDescription(),
                request.getCreated(),
                toItemInfoDto(request.getItems())
        );
    }

    public static ItemRequestDto.ItemDataDto toItemDataDto(Item item) {
        return new ItemRequestDto.ItemDataDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId());
    }

    public static List<ItemRequestDto.ItemDataDto> toItemInfoDto(List<Item> items) {
        return items.stream()
                .map(ItemRequestMapper::toItemDataDto)
                .collect(Collectors.toList());
    }

    public static RequestDto toRequestDto(ItemRequest itemRequest) {
        return new RequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated());
    }
}
