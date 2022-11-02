package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.item.model.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.model.ItemMapper.toItemWithBookingDto;

@SpringBootTest
class ItemServiceIntTest {

    private final ItemController itemController;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceIntTest(ItemController itemController, ItemService itemService, ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.itemController = itemController;
        this.itemRepository = itemRepository;
        this.itemService = itemService;
        this.userRepository = userRepository;
    }

    @Test
    void getItemTest() throws CreatingException {
        User user = new User(1L, "user1", "user1@mail.ru");
        userRepository.save(user);

        Item item = new Item(1L, "Лопата", "Помагает создавать дыры в земле", true, user, null);

        ItemDto itemDto = toItemDto(item);
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);

        ItemWithBookingDto itemWithBookingDto = toItemWithBookingDto(item, null, null, new ArrayList<>());

        ItemDto controllerTest = itemController.createItem(item.getOwner().getId(), itemDto);
        ItemWithBookingDto serviceTest = itemService.getItem(item.getOwner().getId(), item.getId());

        assertEquals(itemDto.getId(), controllerTest.getId());
        assertEquals(itemDto.getName(), controllerTest.getName());
        assertEquals(itemDto.getDescription(), controllerTest.getDescription());
        assertEquals(itemDto.getAvailable(), controllerTest.getAvailable());
        assertEquals(itemDto.getRequestId(), controllerTest.getRequestId());

        assertEquals(serviceTest.getId(), itemWithBookingDto.getId());
        assertEquals(serviceTest.getName(), itemWithBookingDto.getName());
        assertEquals(serviceTest.getDescription(), itemWithBookingDto.getDescription());
        assertEquals(serviceTest.getAvailable(), itemWithBookingDto.getAvailable());
        assertEquals(serviceTest.getNextBooking(), itemWithBookingDto.getNextBooking());
        assertEquals(serviceTest.getLastBooking(), itemWithBookingDto.getLastBooking());
        assertEquals(serviceTest.getComments(), itemWithBookingDto.getComments());

        assertTrue(itemRepository.existsById(item.getId()));
    }

}
