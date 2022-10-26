package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;

    Item item;
    ItemDto itemDto;
    User user;


    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository, itemRequestRepository);

        user = new User(1L, "test", "test@gmail.com");
        item = new Item(1L, "Лопата", "Помагает создавать дыры в земле", true,user, null);

        itemDto = new ItemDto();
        itemDto.setId(null);
        itemDto.setName("Лопата");
        itemDto.setDescription("Помагает создавать дыры в земле");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
//        booking = new Booking(
//                1L,
//                LocalDateTime.of(2022, 11, 29, 0, 0),
//                LocalDateTime.of(2022, 11, 30, 0, 0),
//                null,
//                null,
//                BookingStatus.WAITING
//        );
//
//        user = new User(1L, "Andrey", "googlbubu@gmail.ru");
//        item = new Item(2L, "Самосвал", "Закапыватель", true, user, null);
//        userDto = new UserDto(1L, "Andrey", "googlbubu@gmail.ru");
//
//        itemDto = new ItemDto();
//        itemDto.setId(2L);
//        itemDto.setName("Лопата");
//        itemDto.setDescription("Помагает создавать дыры в земле");
//        itemDto.setAvailable(true);
//        itemDto.setRequestId(3L);
//
//        comment = new Comment(4L, "kekw", null, null, LocalDateTime.of(2023, 6, 1, 0, 0));
//        commentDto = new CommentDto(4L, "kekw");
    }


    @Test
    void createItemTest() throws NotFoundParameterException, CreatingException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto test = itemService.createItem(user.getId(), itemDto);

        assertNotNull(test);
        assertEquals(item.getId(), test.getId());
        assertEquals(itemDto.getName(), test.getName());
        assertEquals(itemDto.getDescription(), test.getDescription());
        assertEquals(itemDto.getAvailable(), test.getAvailable());
        assertEquals(itemDto.getRequestId(), test.getRequestId());
    }

    @Test
    void updateItemTest() throws NotFoundParameterException {
        User user = new User(1L, "test", "test@gmail.com");
        Item item = new Item(1L, "test", "test", true, user, null);
        Item itemUpd = new Item(1L, "testUpdate", "testUpdate", true, user, null);
       // ItemDto itemDto = new ItemDto(null, "testUpdate", "testUpdate", true, null);
        itemDto = new ItemDto();
        itemDto.setId(null);
        itemDto.setName("testUpdate");
        itemDto.setDescription("testUpdate");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);



        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(any(Item.class)))
                .thenReturn(itemUpd);

        ItemDto foundItem = itemService.updateItem(user.getId(), item.getId(), itemDto);

        Assertions.assertNotNull(foundItem);
        Assertions.assertEquals(item.getId(), foundItem.getId());
        Assertions.assertEquals(itemDto.getName(), foundItem.getName());
        Assertions.assertEquals(itemDto.getDescription(), foundItem.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), foundItem.getAvailable());
        Assertions.assertEquals(itemDto.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void getAllByUserId() {
    }

    @Test
    void getItem() {
    }

    @Test
    void searchAvailableItem() {
    }

    @Test
    void addComment() {
    }

    @Test
    void delete() {
    }
}