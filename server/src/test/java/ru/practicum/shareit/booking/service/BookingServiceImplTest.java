package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.IncorrectStatusException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.BookingMapper.toBookingWithItemAndUserDto;
import static ru.practicum.shareit.booking.model.BookingStatus.checkBookingStatus;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    private Item item;
    private Item item2;
    private Item item3;
    private User user;
    private User user2;
    private BookingDto bookingDto;
    private Booking booking;
    private Booking bookingApproved;
    private Booking booking2;
    private Booking booking3;
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
        item3 = new Item(2L, "Бетономешалка>", "Мешает бетон!", true, user2, null);

        booking = new Booking(
                1L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                item,
                user,
                BookingStatus.WAITING
        );

        bookingApproved = new Booking(
                1L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                item,
                user,
                BookingStatus.APPROVED
        );

        booking2 = new Booking(
                2L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                item,
                user2,
                BookingStatus.WAITING
        );

        booking3 = new Booking(
                3L,
                LocalDateTime.of(2077, 7, 7, 7, 7),
                LocalDateTime.of(2077, 8, 2, 3, 1),
                item3,
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
        when(bookingRepository.findBookingsByBookerIdOrderByStartDesc(user.getId(), pages)).thenReturn(bookings);

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
    void getBookingByBookerPastTest() {
        booking.setStatus(BookingStatus.PAST);
        Pageable pages = Pageable.ofSize(10);
        List<Booking> bookings = List.of(booking);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByBooker(user.getId(), BookingStatus.PAST, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.PAST));
    }

    @Test
    void getBookingByBookerCurrentTest() {
        booking.setStatus(BookingStatus.CURRENT);
        Pageable pages = Pageable.ofSize(10);
        List<Booking> bookings = List.of(booking);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingBookerByCurrentState(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByBooker(user.getId(), BookingStatus.CURRENT, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.CURRENT));
    }

    @Test
    void getBookingByBookerFutureTest() {
        booking.setStatus(BookingStatus.FUTURE);
        Pageable pages = Pageable.ofSize(10);
        List<Booking> bookings = List.of(booking);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByBookerIdAndEndAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByBooker(user.getId(), BookingStatus.FUTURE, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.FUTURE));
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
    void getBookingByItemOwnerBookingStatusWaitingTest() {
        Pageable pages = Pageable.ofSize(10);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.WAITING, pages);

        assertThat(test.size(), equalTo(0));
    }

    @Test
    void getBookingByItemOwnerBookingStatusRejectedTest() {
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

    @Test
    void getBookingByItemOwnerBookingStatusPastTest() {
        booking.setStatus(BookingStatus.PAST);
        List<Booking> bookings = List.of(booking);
        Pageable pages = Pageable.ofSize(10);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.PAST, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.PAST));
    }

    @Test
    void getBookingByItemOwnerBookingStatusCurrentTest() {
        booking.setStatus(BookingStatus.CURRENT);
        List<Booking> bookings = List.of(booking);
        Pageable pages = Pageable.ofSize(10);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingOwnerByCurrentState(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.CURRENT, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.CURRENT));
    }

    @Test
    void getBookingByItemOwnerBookingStatusFutureTest() {
        booking.setStatus(BookingStatus.FUTURE);
        List<Booking> bookings = List.of(booking);
        Pageable pages = Pageable.ofSize(10);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingsByItemOwnerIdAndEndAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingWithItemAndUserDto> test = bookingService.getBookingByItemOwner(user.getId(), BookingStatus.FUTURE, pages);

        assertThat(test.get(0).getId(), equalTo(bookingDto.getId()));
        assertThat(test.get(0).getStart(), equalTo(bookingDto.getStart()));
        assertThat(test.get(0).getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(test.get(0).getStatus(), equalTo(BookingStatus.FUTURE));
    }

    @Test
    void getBookingByBookerFailTest() throws NotFoundParameterException {

        Exception exception = Assertions.assertThrows(IncorrectStatusException.class,
                () -> bookingService.getBookingByBooker(1L, checkBookingStatus("kek"), Pageable.ofSize(10)));

        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }


    @Test
    void getBookingByBookerFailAgainTest() throws NotFoundParameterException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.getBookingByBooker(user.getId(), BookingStatus.ALL, Pageable.ofSize(10)));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void getByIdFailTest() {
        when(bookingRepository.findById(booking3.getId())).thenReturn(Optional.of(booking3));

        when(itemRepository.findById(booking3.getItem().getId())).thenReturn(Optional.of(item3));

        when(userRepository.findById(booking3.getBooker().getId())).thenReturn(Optional.of(user2));

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.getById(user.getId(), booking3.getId()));

        Assertions.assertEquals("Exception: booking user not found.", exception.getMessage());
    }

    @Test
    void getByIdUserFailTest() {
        when(bookingRepository.findById(booking3.getId())).thenReturn(Optional.of(booking3));

        when(itemRepository.findById(booking3.getItem().getId())).thenReturn(Optional.of(item3));

        when(userRepository.findById(booking3.getBooker().getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.getById(user.getId(), booking3.getId()));

        Assertions.assertEquals("Exception: booker not found.", exception.getMessage());
    }

    @Test
    void getByIdItemFailTest() {
        when(bookingRepository.findById(booking3.getId())).thenReturn(Optional.of(booking3));

        when(itemRepository.findById(booking3.getItem().getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.getById(user.getId(), booking3.getId()));

        Assertions.assertEquals("Exception: booking item not found.", exception.getMessage());
    }

    @Test
    void getByIdBookingFailTest() {
        when(bookingRepository.findById(booking3.getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.getById(user.getId(), booking3.getId()));

        Assertions.assertEquals("Exception: booking not found.", exception.getMessage());
    }

    @Test
    void addBookingUserFailTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.addBooking(user.getId(), bookingDto));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void addBookingItemFailTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.addBooking(user.getId(), bookingDto));

        Assertions.assertEquals("Exception: Item doesn't exists.", exception.getMessage());
    }

    @Test
    void addBookingAvailableFailTest() {
        item.setAvailable(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));

        Exception exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> bookingService.addBooking(user.getId(), bookingDto));

        Assertions.assertEquals("Exception: Item is not available.", exception.getMessage());
    }

    @Test
    void addBookingOwnerFailTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item2));

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.addBooking(user.getId(), bookingDto));

        Assertions.assertEquals("Exception: Item has another owner.", exception.getMessage());
    }

    @Test
    void approveBookingFailTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.approve(user.getId(), booking.getId(), true));

        Assertions.assertEquals("Exception: Booking not found.", exception.getMessage());
    }

    @Test
    void approveStatusFailTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(bookingApproved));

        Exception exception = Assertions.assertThrows(IncorrectParameterException.class,
                () -> bookingService.approve(user.getId(), booking.getId(), true));

        Assertions.assertEquals("Exception: Wrong booking status \"APPROVED\".", exception.getMessage());
    }

    @Test
    void approveUserFailTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.approve(user.getId(), booking.getId(), true));

        Assertions.assertEquals("Exception: Wrong user id.", exception.getMessage());
    }

    @Test
    void approveItemFailTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(booking.getItem().getId())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.approve(user.getId(), booking.getId(), true));

        Assertions.assertEquals("Exception: Wrong item id.", exception.getMessage());
    }

    @Test
    void approveOwnerFailTest() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        Exception exception = Assertions.assertThrows(NotFoundParameterException.class,
                () -> bookingService.approve(user.getId(), booking.getId(), true));

        Assertions.assertEquals("Exception: Item - owner not found.", exception.getMessage());
    }

    @Test
    void approveNotApprovedFailTest() {
        //    booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingWithItemAndUserDto dto = toBookingWithItemAndUserDto(booking);

        BookingWithItemAndUserDto test = bookingService.approve(user2.getId(), booking.getId(), false);

        assertNotNull(test);
        assertEquals(dto.getId(), test.getId());
        assertEquals(dto.getBooker().getId(), test.getBooker().getId());
        assertEquals(dto.getItem().getId(), test.getItem().getId());
    }
}