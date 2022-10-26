package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    Item item;
    Item item2;
    User user;
    User user2;
    private BookingDto bookingDto;
    private Booking booking;
    private UserDto userDto;
    private BookingService bookingService;
    private MockitoSession mockitoSession;

    @BeforeEach
    void init() {
        mockitoSession = Mockito.mockitoSession().initMocks(this).startMocking();
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        userDto = new UserDto(2L, "Bob", "bob@mail.ru");
        user = new User(1L, "test", "test@gmail.com");
        user2 = new User(2L, "test2", "test2@gmail.com");

        item = new Item(1L, "Лопата", "Помагает создавать дыры в земле", true, user2, null);
        item2 = new Item(2L, "Бетономешалка>", "Мешает бетон!", true, user, null);
        booking = new Booking(
                1L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                item,
                user,
                BookingStatus.WAITING
        );

        bookingDto = new BookingDto(
                1L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                1L
        );

    }

    @AfterEach
    void tearDown() {
        mockitoSession.finishMocking();
    }

    @Test
    void getById() {
    }

    @Test
    void addBookingTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(ArgumentMatchers.any(Booking.class))).thenReturn(booking);

        BookingWithItemAndUserDto bookingTest = bookingService.addBooking(1L, bookingDto);

        MatcherAssert.assertThat(bookingTest.getId(), equalTo(bookingDto.getId()));
        MatcherAssert.assertThat(bookingTest.getStart(), equalTo(bookingDto.getStart()));
        MatcherAssert.assertThat(bookingTest.getEnd(), equalTo(bookingDto.getEnd()));
        MatcherAssert.assertThat(bookingTest.getStatus(), equalTo(BookingStatus.WAITING));

    }

    @Test
    void approve() {
    }

    @Test
    void getBookingByBooker() {
    }

    @Test
    void getBookingByItemOwner() {
    }
}