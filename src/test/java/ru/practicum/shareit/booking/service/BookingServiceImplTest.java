package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
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
    private Booking booking2;
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

        booking2 = new Booking(
                1L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                item,
                user2,
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
    void getByIdTest() throws CreatingException {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        BookingWithItemAndUserDto test = bookingService.getById(userDto.getId(), bookingDto.getId());

        assertThat(test.getId(), equalTo(bookingDto.getId()));
        assertThat(test.getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void addBookingTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingWithItemAndUserDto test = bookingService.addBooking(1L, bookingDto);

        assertThat(test.getId(), equalTo(bookingDto.getId()));
        assertThat(test.getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void approve() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking2));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingWithItemAndUserDto test = bookingService.approve(userDto.getId(), bookingDto.getId(), true);

        assertThat(test.getId(), equalTo(bookingDto.getId()));
        assertThat(test.getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.getEnd(), equalTo(bookingDto.getEnd()));
    }

    @Test
    void getBookingByBookerALLTest() {
        Pageable pages = Pageable.ofSize(10);
        List<Booking> bookings = List.of(booking);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByBookerIdOrderByStartDesc(user.getId(), pages)).thenReturn(bookings);;

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByBooker(user.getId(), BookingStatus.ALL, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void getBookingByBookerREJECTEDTest() {
        booking.setStatus(BookingStatus.REJECTED);
        Pageable pages = Pageable.ofSize(10);
        List<Booking> bookings = List.of(booking);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(user.getId(), BookingStatus.REJECTED, pages)).thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByBooker(user.getId(), BookingStatus.REJECTED, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.REJECTED));
    }

    @Test
    void getBookingByItemOwnerBookingStatusAllTest() {
        booking.setStatus(BookingStatus.ALL);
        List<Booking> bookings = List.of(booking);
        Pageable pages = Pageable.ofSize(10);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(user.getId(), pages)).thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.ALL, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.ALL));
    }

    @Test
    void getBookingByItemOwnerBookingStatusWAITINGTest() {
        Pageable pages = Pageable.ofSize(10);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.WAITING, pages);

        assertThat(test.size(), equalTo(0));
    }

    @Test
    void getBookingByItemOwnerBookingStatusREJECTEDTest() {
        booking.setStatus(BookingStatus.REJECTED);
        List<Booking> bookings = List.of(booking);
        Pageable pages = Pageable.ofSize(10);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(user.getId(), BookingStatus.REJECTED, pages)).thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.REJECTED, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.REJECTED));
    }
}