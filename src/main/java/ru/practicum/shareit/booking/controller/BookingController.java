package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingWithItemAndUserDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) throws NotFoundParameterException, CreatingException {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingWithItemAndUserDto> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(required = false, defaultValue = "ALL") String state,
                                                              @RequestParam(value = "from", defaultValue = "0")
                                                              @PositiveOrZero int from,
                                                              @RequestParam(value = "size", defaultValue = "10")
                                                              @Positive int size) throws NotFoundParameterException {
        Pageable pages = PageRequest.of(from / size, size);
        return bookingService.getBookingByBooker(userId, state, pages);
    }

    @GetMapping("/owner")
    public List<BookingWithItemAndUserDto> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam(required = false, defaultValue = "ALL") String state,
                                                                 @RequestParam(value = "from", defaultValue = "0")
                                                                 @PositiveOrZero int from,
                                                                 @RequestParam(value = "size", defaultValue = "10")
                                                                 @Positive int size) throws NotFoundParameterException {
        Pageable pages = PageRequest.of(from / size, size);
        return bookingService.getBookingByItemOwner(userId, state, pages);
    }

    @PostMapping
    public BookingWithItemAndUserDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody BookingDto bookingDto) throws NotFoundParameterException {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingWithItemAndUserDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) throws NotFoundParameterException {
        return bookingService.approve(userId, bookingId, approved);
    }
}
