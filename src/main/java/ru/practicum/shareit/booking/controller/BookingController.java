package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingWithItemAndUserDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId) throws NotFoundParameterException, CreatingException {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingWithItemAndUserDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(required = false, defaultValue = "ALL") String state) throws NotFoundParameterException {
        return bookingService.getAllBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingWithItemAndUserDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(required = false, defaultValue = "ALL") String state) throws NotFoundParameterException {
        return bookingService.getAllByOwner(userId, state);
    }

    @PostMapping
    public BookingWithItemAndUserDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody BookingDto bookingDto) throws NotFoundParameterException, CreatingException {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingWithItemAndUserDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam boolean approved) throws NotFoundParameterException {
        return bookingService.approve(userId, bookingId, approved);
    }
}
