package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.IncorrectStatusException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.model.BookingMapper.toBookingWithItemAndUserDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingWithItemAndUserDto getById(Long userId, Long bookingId) throws NotFoundParameterException {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundParameterException("Exception: booking not found."));

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new NotFoundParameterException("Exception: booking item not found."));

        User user = userRepository.findById(booking.getBooker().getId()).orElseThrow(() ->
                new NotFoundParameterException("Exception: booker not found."));

        if (!(user.getId().equals(userId) || item.getOwner().getId().equals(userId)))
            throw new NotFoundParameterException("Exception: booking user not found.");

        return toBookingWithItemAndUserDto(booking);
    }

    @Override
    public BookingWithItemAndUserDto addBooking(Long userId, BookingDto bookingDto) throws NotFoundParameterException {

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()))
            throw new IncorrectParameterException("Booking time is incorrect");

        if (bookingDto.getStart().isBefore(LocalDateTime.now()))
            throw new IncorrectParameterException("Booking time is incorrect");

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("User doesn't exists"));

        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new NotFoundParameterException("Item doesn't exists"));

        if (item.getAvailable().equals(false))
            throw new IncorrectParameterException("Item is not available");

        if (item.getOwner().equals(user))
            throw new NotFoundParameterException("Item has another owner");

        Booking booking = toBooking(bookingDto, user, item, BookingStatus.WAITING);

        return toBookingWithItemAndUserDto(bookingRepository.save(booking));
    }

    @Override
    public BookingWithItemAndUserDto approve(Long userId, Long bookingId, Boolean approved) throws NotFoundParameterException {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundParameterException("Booking info not found"));

        if (booking.getStatus().equals(BookingStatus.APPROVED))
            throw new IncorrectParameterException(String.format(
                    "Exception: Wrong booking status \"%s\".", booking.getStatus()
            ));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("User doesn't exists"));

        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new NotFoundParameterException("Item doesn't exists"));

        if (!item.getOwner().equals(user))
            throw new NotFoundParameterException("User's booking information was not found");

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return toBookingWithItemAndUserDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingWithItemAndUserDto> getBookingByBooker(Long userId, String state) throws NotFoundParameterException {

        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("User doesn't exists"));

        BookingStatus bookingStatus = BookingStatus.checkBookingStatus(state);

        switch (bookingStatus) {
            case ALL:
                return bookingRepository.findBookingsByBookerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case WAITING:

            case REJECTED:
                return bookingRepository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, bookingStatus)
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findBookingsByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case CURRENT:
                return bookingRepository.findBookingBookerByCurrentState(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findBookingsByBookerIdAndEndAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            default:
                throw new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingWithItemAndUserDto> getBookingByItemOwner(Long userId, String state) throws NotFoundParameterException {

        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("User doesn't exists"));

        BookingStatus bookingStatus = BookingStatus.checkBookingStatus(state);

        switch (bookingStatus) {
            case ALL:
                return bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(userId)
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case WAITING:

            case REJECTED:
                return bookingRepository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, bookingStatus)
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case PAST:
                return bookingRepository.findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case CURRENT:
                return bookingRepository.findBookingOwnerByCurrentState(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findBookingsByItemOwnerIdAndEndAfterOrderByStartDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingWithItemAndUserDto)
                        .collect(Collectors.toList());

            default:
                throw new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}