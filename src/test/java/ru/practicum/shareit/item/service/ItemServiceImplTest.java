package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;

    private Item item;
    private User user;
    private User user2;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private MockitoSession mockitoSession;

    @BeforeEach
    void beforeEach() {
        mockitoSession = Mockito.mockitoSession().initMocks(this).startMocking();
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository, itemRequestRepository);

        user = new User(1L, "test", "test@gmail.com");
        user2 = new User(2L, "test2", "test2@gmail.com");
        item = new Item(1L, "Лопата", "Помагает создавать дыры в земле", true, user, null);
        itemDto = new ItemDto();
        itemDto.setId(null);
        itemDto.setName("Лопата");
        itemDto.setDescription("Помагает создавать дыры в земле");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);
        commentDto = new CommentDto(1L, "mew");
    }

    @AfterEach
    void afterEach() {
        mockitoSession.finishMocking();
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
    void createItemWithRequestTest() throws NotFoundParameterException, CreatingException {
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemDto.setRequestId(1L);
        ItemRequest itemRequest = new ItemRequest(1L, "item request", user, LocalDateTime.now(), items);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(itemDto.getRequestId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto test = itemService.createItem(user.getId(), itemDto);

        assertNotNull(test);
        assertEquals(item.getId(), test.getId());
        assertEquals(itemDto.getName(), test.getName());
        assertEquals(itemDto.getDescription(), test.getDescription());
        assertEquals(itemDto.getAvailable(), test.getAvailable());
    }

    @Test
    void createItemWithRequestFailTest() {
        itemDto.setRequestId(3L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(NotFoundParameterException.class,
                () -> itemService.createItem(user.getId(), itemDto));

        assertEquals("Exception: Request not found.", exception.getMessage());//не работает
    }

    @Test
    void addCommentTest() throws CreatingException {
        Comment comment = new Comment(1L, "test", item, user, LocalDateTime.now());
        CommentDto commentDto = new CommentDto(null, "test");
        CommentWithAuthorAndItemDto commentInfoDto = new CommentWithAuthorAndItemDto(1L, "test", "test", LocalDateTime.now());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(true);

        CommentWithAuthorAndItemDto test = itemService.addComment(1L, 1L, commentDto);

        assertNotNull(test);
        assertEquals(commentInfoDto.getId(), test.getId());
        assertEquals(commentInfoDto.getText(), test.getText());
        assertEquals(commentInfoDto.getAuthorName(), test.getAuthorName());
    }

    @Test
    void updateItemTest() throws NotFoundParameterException {
        Item itemUpd = new Item(1L, "меч", "резать бананы", true, user, null);

        ItemDto newItemDto = new ItemDto();
        itemDto.setId(null);
        itemDto.setName("меч");
        itemDto.setDescription("резать бананы");
        itemDto.setAvailable(true);
        itemDto.setRequestId(null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(itemUpd);

        ItemDto test = itemService.updateItem(user.getId(), item.getId(), newItemDto);

        assertNotNull(test);
        assertEquals(item.getId(), test.getId());
        assertEquals(itemDto.getName(), test.getName());
        assertEquals(itemDto.getDescription(), test.getDescription());
        assertEquals(itemDto.getAvailable(), test.getAvailable());
        assertEquals(itemDto.getRequestId(), test.getRequestId());
    }

    @Test
    void searchAvailableItemTest() {
        String text = "";

        List<ItemDto> results = itemService.searchAvailableItem(text);

        Assertions.assertEquals(0, results.size());
    }

    @Test
    void deleteTest() {
        itemService.delete(user.getId(), item.getId());
        verify(itemRepository, times(1)).deleteById(item.getId());
    }

    @Test
    void getItemTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemWithBookingDto test = itemService.getItem(user.getId(), item.getId());

        assertThat(test.getId(), equalTo(item.getId()));
        assertThat(test.getName(), equalTo(item.getName()));
        assertThat(test.getDescription(), equalTo(item.getDescription()));
        assertThat(test.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void shouldGetAllByUserTest() {
        itemService.getAllByUserId(user.getId());
        verify(itemRepository, times(1)).findAllById(item.getId());
    }

    @Test
    void createItemFailTest() throws NotFoundParameterException {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> itemService.createItem(user.getId(), itemDto));

        Assertions.assertEquals("Exception: Owner item id.", exception.getMessage());
    }

    @Test
    void updateUserFailTest() throws NotFoundParameterException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> itemService.updateItem(user.getId(), item.getId(), itemDto));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void updateItemFailTest() throws NotFoundParameterException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> itemService.updateItem(user.getId(), item.getId(), itemDto));

        Assertions.assertEquals("Exception: Wrong item id.", exception.getMessage());
    }

    @Test
    void updateOwnerFailTest() throws NotFoundParameterException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> itemService.updateItem(user2.getId(), item.getId(), itemDto));

        Assertions.assertEquals("Exception: You must be the owner of an item to upgrade it.", exception.getMessage());
    }

    @Test
    void getItemFailTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> itemService.getItem(user.getId(), item.getId()));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());

    }

    @Test
    void getFailItemTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> itemService.getItem(user.getId(), item.getId()));

        Assertions.assertEquals("Exception: Wrong item id.", exception.getMessage());
    }

    @Test
    void addCommentUserFailTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> itemService.addComment(user.getId(), item.getId(), commentDto));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void addCommentItemFailTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> itemService.addComment(user.getId(), item.getId(), commentDto));

        Assertions.assertEquals("Exception: Wrong item id.", exception.getMessage());
    }

    @Test
    void addCommentExistsBookingByItemIdAndBookerIdAndEndBeforeFailTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(false);

        Exception exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> itemService.addComment(user.getId(), item.getId(), commentDto));

        Assertions.assertEquals("The user has not used this item yet.", exception.getMessage());
    }

    @Test
    void addCommentTextFailTest() {
        commentDto.setText("");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(true);

        Exception exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> itemService.addComment(user.getId(), item.getId(), commentDto));

        Assertions.assertEquals("Exception: Comment cannot be empty.", exception.getMessage());
    }
}