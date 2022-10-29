package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.mapper.ItemRequestMapper.*;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto add(Long userId, ItemRequestDescriptionDto itemRequestDescriptionDto) throws NotFoundParameterException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong user id."));

        return toRequestDto(itemRequestRepository.save(toItemRequest(itemRequestDescriptionDto, user)));
    }

    @Override
    public List<ItemRequestDto> get(Long userId, PageRequest pages) throws NotFoundParameterException {
        userCheck(userId);

        return itemRequestRepository.findRequests(userId, pages)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getById(Long userId, Long requestId) throws NotFoundParameterException {
        userCheck(userId);
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong request id."));

        return toItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getByUser(Long userId) throws NotFoundParameterException {
        userCheck(userId);

        return itemRequestRepository.findItemRequestsByRequesterId(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    private void userCheck(Long userId) throws NotFoundParameterException {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong user id."));
    }
}