package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;

import java.util.List;

public interface BookingService {

    BookingWithItemAndUserDto getById(Long userId, Long bookingId) throws NotFoundParameterException, CreatingException;

    List<BookingWithItemAndUserDto> getBookingByBooker(Long userId, BookingStatus status, Pageable pages) throws NotFoundParameterException;

    List<BookingWithItemAndUserDto> getBookingByItemOwner(Long userId, BookingStatus status, Pageable pages) throws NotFoundParameterException;

    BookingWithItemAndUserDto addBooking(Long userId, BookingDto bookingDto) throws NotFoundParameterException;

    BookingWithItemAndUserDto approve(Long userId, Long bookingId, Boolean approved) throws NotFoundParameterException;

}
