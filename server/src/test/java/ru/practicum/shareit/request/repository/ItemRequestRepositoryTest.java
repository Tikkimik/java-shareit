package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user;
    private ItemRequest request1;
    private ItemRequest request3;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        User user2 = userRepository.save(new User(null, "test2", "test2@mail.ru"));
        itemRepository.save(new Item(null, "test", "test", true, user2, request1));
        request1 = itemRequestRepository.save(new ItemRequest(null, "test1", user, LocalDateTime.now(), null));
        itemRequestRepository.save(new ItemRequest(null, "test2", user, LocalDateTime.now(), null));
        request3 = itemRequestRepository.save(new ItemRequest(null, "test3", user2, LocalDateTime.now(), null));
    }

    @Test
    void findRequestsTest() {
        List<ItemRequest> requests = itemRequestRepository.findRequests(user.getId(), Pageable.unpaged());

        Assertions.assertFalse(requests.isEmpty());
        Assertions.assertEquals(1, requests.size());
        Assertions.assertEquals(request3.getId(), requests.get(0).getId());
    }

    @Test
    void findItemRequestsByRequesterIdTest() {
        List<ItemRequest> requests = itemRequestRepository.findItemRequestsByRequesterId(user.getId());

        Assertions.assertFalse(requests.isEmpty());
        Assertions.assertEquals(2, requests.size());
    }
}