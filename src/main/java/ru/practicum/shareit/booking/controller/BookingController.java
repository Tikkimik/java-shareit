package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingWithItemAndUserDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) throws NotFoundParameterException, CreatingException {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingWithItemAndUserDto> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(required = false, defaultValue = "ALL") String state) throws NotFoundParameterException {
        return bookingService.getBookingByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingWithItemAndUserDto> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam(required = false, defaultValue = "ALL") String state) throws NotFoundParameterException {
        return bookingService.getBookingByItemOwner(userId, state);
    }

    @PostMapping
    public BookingWithItemAndUserDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody BookingDto bookingDto) throws NotFoundParameterException, CreatingException {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingWithItemAndUserDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) throws NotFoundParameterException, CreatingException {
        return bookingService.approve(userId, bookingId, approved);
    }
}
