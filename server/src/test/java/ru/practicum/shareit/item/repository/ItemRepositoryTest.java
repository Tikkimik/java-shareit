package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User(null, "Ричард", "Haselhoff007@chsv.us"));
        itemRepository.save(new Item(null, "Бензопила", "Лучший инструмент во времена апокалипсиса.", true, user, null));
    }

    @Test
    void shouldReturnItemListWhereConsistKeyword() {
        List<Item> results = itemRepository.search("инструмент во");

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void shouldReturnEmptyItemListWhenNotExistItemsWithKeyword() {
        List<Item> results = itemRepository.search("test");

        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    void shouldReturnItemListByUserId() {
        List<Item> results = itemRepository.findAllById(user.getId());

        assertNotNull(results);
        assertEquals(1, results.size());
    }
}