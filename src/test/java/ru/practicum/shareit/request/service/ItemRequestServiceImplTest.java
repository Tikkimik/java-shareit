package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.request.dto.ItemRequestDescriptionDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {

    private UserRepository userRepository;
    private ItemRequestService itemRequestService;
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository);
    }

    @Test
    void returnRequestDtoTest() {
        ItemRequestDescriptionDto itemRequestDto = new ItemRequestDescriptionDto("test");
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        RequestDto test = itemRequestService.add(user.getId(), itemRequestDto);

        Assertions.assertNotNull(test);
        Assertions.assertEquals(itemRequestDto.getDescription(), test.getDescription());
    }

    @Test
    void notFoundParameterExceptionAddTest() {
        ItemRequestDescriptionDto itemRequestDto = new ItemRequestDescriptionDto("test");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundParameterException.class,
                () -> itemRequestService.add(1L, itemRequestDto)
        );
        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void shouldReturnListUsingGetRequestsByUserWithRightParameters() {
        List<ItemRequest> requests = List.of(new ItemRequest(1L, "test", null, LocalDateTime.now(), new ArrayList<>()));
        User user = new User(1L, "test", "test@gmail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findItemRequestsByRequesterId(anyLong())).thenReturn(requests);

        List<ItemRequestDto> test = itemRequestService.getByUser(1L);

        Assertions.assertNotNull(test);
        Assertions.assertEquals(1, test.size());
    }

    @Test
    void notFoundParameterExceptionGetByUserTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundParameterException.class,
                () -> itemRequestService.getByUser(1L)
        );
        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionUsingRequestByIdWithRightParameters() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest itemRequest = new ItemRequest(1L, "test", user, LocalDateTime.now(), new ArrayList<>());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequestDto test = itemRequestService.getById(user.getId(), itemRequest.getId());
        Assertions.assertNotNull(test);
    }

    @Test
    void notFoundParameterExceptionGetByIdTest() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest itemRequest = new ItemRequest(1L, "test", user, LocalDateTime.now(), null);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundParameterException.class,
                () -> itemRequestService.getById(user.getId(), itemRequest.getId()));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void notFoundParameterExceptionWrongRequestTest() {
        User user = new User(1L, "test", "test@gmail.com");
        ItemRequest itemRequest = new ItemRequest(1L, "test", user, LocalDateTime.now(), null);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                NotFoundParameterException.class,
                () -> itemRequestService.getById(user.getId(), itemRequest.getId()));

        Assertions.assertEquals("Exception: Wrong request id.", exception.getMessage());
    }
}