package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.exceptions.IncorrectParameterException;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;


    @Override
    public BookingWithItemAndUserDto getBooking(Long userId, Long bookingId) throws NotFoundParameterException, CreatingException {
        checkUserById(userId);
        checkBookingById(bookingId);

        BookingDto bookingDto = bookingMapper.toBookingDto(bookingRepository.getReferenceById(bookingId));

        Item item = itemRepository.getReferenceById(bookingDto.getItemId());

        if (!Objects.equals(bookingDto.getBookerId(), userId) && !Objects.equals(item.getOwner(), userId))
            throw new NotFoundParameterException(String.format(
                    "Exception: Wrong user id = \"%s\", booker id = \"%b\".", userId, bookingDto.getBookerId()
            ));

        checkBookingTimings(bookingDto);
        checkItem(bookingDto.getItemId());

        return getBookingWithItemAndUserDto(bookingDto);
    }

    @Override
    public BookingWithItemAndUserDto addBooking(Long userId, BookingDto bookingDto) throws NotFoundParameterException, CreatingException {
        checkUserById(userId);
        checkBookingTimings(bookingDto);
        checkItem(bookingDto.getItemId());

        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        checkOwner(item, userId);

        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setBookerId(userId);

        bookingRepository.save(bookingMapper.toBooking(bookingDto));

        return getBookingWithItemAndUserDto(bookingDto);
    }

    @Override
    public BookingWithItemAndUserDto approve(Long userId, Long bookingId, boolean approved) throws NotFoundParameterException {
        Booking booking = bookingRepository.getReferenceById(bookingId);

        if (booking.getStatus().equals(BookingStatus.APPROVED))
            throw new IncorrectParameterException(String.format(
                    "Exception: Wrong booking status \"%s\".", booking.getStatus()
            ));

        Item item = itemRepository.getReferenceById(booking.getItemId());

        checkNotOwner(item, userId);

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        bookingRepository.save(booking);
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);
        return getBookingWithItemAndUserDto(bookingDto);
    }

    @Override
    public List<BookingWithItemAndUserDto> getAllBookings(Long userId, String state) throws NotFoundParameterException {
        checkUserById(userId);

        if (state.equals("ALL")) {
            return listOfBookingWithItemAndUserDto(bookingRepository.findAllByBookerIdOrderByStartDesc(userId));
        }

        return listOfBookingWithItemAndUserDto(checkStatus(new ArrayList<>(
                bookingRepository.findAllByBookerIdOrderByStartDesc(userId)), state)
        );
    }

    @Override
    public List<BookingWithItemAndUserDto> getAllByOwner(Long userId, String state) throws NotFoundParameterException {
        checkUserById(userId);

        List<Item> items = itemRepository.findAllByOwnerOrderById(userId);
        List<Booking> bookings = new ArrayList<>();

        for (Item i : items) {
            bookings.addAll(bookingRepository.findAllByItemIdOrderByStartDesc(i.getId()));
        }

        if (state.equals("ALL")) return listOfBookingWithItemAndUserDto(bookings);

        return listOfBookingWithItemAndUserDto(checkStatus(bookings, state));
    }

    private void checkUserById(Long userId) throws NotFoundParameterException {
        if (!userRepository.existsById(userId))
            throw new NotFoundParameterException("Exception: Wrong user id.");
    }

    private void checkOwner(Item item, Long userId) throws NotFoundParameterException {
        if (Objects.equals(userId, item.getOwner()))
            throw new NotFoundParameterException("Exception: User can't request for this item.");
    }

    private void checkNotOwner(Item item, Long userId) throws NotFoundParameterException {
        if (!Objects.equals(userId, item.getOwner()))
            throw new NotFoundParameterException("Exception: User can't request for this item.");
    }

    private void checkItem(Long itemId) throws CreatingException, NotFoundParameterException {
        if (!itemRepository.existsById(itemId))
            throw new NotFoundParameterException("Exception: Wrong item id.");

        Item item = itemRepository.getReferenceById(itemId);

        if (item.getAvailable().equals(false))
            throw new IncorrectParameterException("Exception: Item status unavailable.");
    }

    private void checkBookingById(Long bookingId) throws NotFoundParameterException {
        if (!bookingRepository.existsById(bookingId))
            throw new NotFoundParameterException("Exception: Wrong booking id.");
    }

    private void checkBookingTimings(BookingDto bookingDto) throws CreatingException {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()))
            throw new IncorrectParameterException("Exception: Booking start after end booking.");

        if (bookingDto.getStart().isBefore(LocalDateTime.now()))
            throw new IncorrectParameterException("Exception: booking cannot start in the past.");
    }

    private List<Booking> checkStatus(List<Booking> bookings, String state) {

        List<Booking> bookingList = new ArrayList<>();

        for (Booking currentBooking : bookings) {
            switch (state) {
                case "CURRENT":
                    if (currentBooking.getEnd().isAfter(LocalDateTime.now())
                            && currentBooking.getStart().isBefore(LocalDateTime.now()))
                        bookingList.add(currentBooking);
                    break;
                case "WAITING":
                    if (currentBooking.getStatus().equals(BookingStatus.WAITING))
                        bookingList.add(currentBooking);
                    break;
                case "REJECTED":
                    if (currentBooking.getStatus().equals(BookingStatus.REJECTED))
                        bookingList.add(currentBooking);
                    break;
                case "PAST":
                    if (currentBooking.getEnd().isBefore(LocalDateTime.now()))
                        bookingList.add(currentBooking);
                    break;
                case "FUTURE":
                    if (currentBooking.getStart().isAfter(LocalDateTime.now()))
                        bookingList.add(currentBooking);
                    break;
                default:
                    throw new IncorrectParameterException("Unknown state: UNSUPPORTED_STATUS");
            }
        }

        return bookingList;
    }

    private BookingWithItemAndUserDto getBookingWithItemAndUserDto(BookingDto bookingDto) {

        BookingWithItemAndUserDto bookingWithItemAndUserDto = bookingMapper.toBookingWithItemAndUserDto(
                bookingMapper.toBookingDto(bookingRepository.findBookingByStartAndEndAndBookerIdAndItemId(
                        bookingDto.getStart(),
                        bookingDto.getEnd(),
                        bookingDto.getBookerId(),
                        bookingDto.getItemId())
                )
        );

        bookingWithItemAndUserDto.setBooker(userMapper.toUserDto(userRepository.getReferenceById(bookingDto.getBookerId())));
        bookingWithItemAndUserDto.setItem(itemMapper.toItemDto(itemRepository.getReferenceById(bookingDto.getItemId())));

        return bookingWithItemAndUserDto;
    }

    private List<BookingWithItemAndUserDto> listOfBookingWithItemAndUserDto(List<Booking> bookings) {
        return bookings
                .stream()
                .map(bookingMapper::toBookingDto)
                .map(this::getBookingWithItemAndUserDto)
                .collect(Collectors.toList());
    }

}
