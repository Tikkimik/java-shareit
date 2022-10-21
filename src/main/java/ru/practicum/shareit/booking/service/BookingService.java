package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;

import java.util.List;

public interface BookingService {

    BookingWithItemAndUserDto getBooking(Long userId, Long bookingId) throws NotFoundParameterException, CreatingException;

    List<BookingWithItemAndUserDto> getAllBookings(Long userId, String state) throws NotFoundParameterException;

    List<BookingWithItemAndUserDto> getAllByOwner(Long userId, String state) throws NotFoundParameterException;

    BookingWithItemAndUserDto addBooking(Long userId, BookingDto bookingDto) throws NotFoundParameterException, CreatingException;

    BookingWithItemAndUserDto approve(Long userId, Long bookingId, boolean approved) throws NotFoundParameterException;

}
