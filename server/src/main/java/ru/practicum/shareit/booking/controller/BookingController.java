package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingWithItemAndUserDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) throws NotFoundParameterException, CreatingException {
        log.info("Get extended booking information");
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingWithItemAndUserDto> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(required = false, defaultValue = "ALL") String state,
                                                              @RequestParam(value = "from", defaultValue = "0") int from,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) throws NotFoundParameterException {
        log.info("Get booking by booker from={}, size={}", from, size);
        BookingStatus status = BookingStatus.checkBookingStatus(state);

        Pageable pages = PageRequest.of(from / size, size);
        return bookingService.getBookingByBooker(userId, status, pages);
    }

    @GetMapping("/owner")
    public List<BookingWithItemAndUserDto> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam(required = false, defaultValue = "ALL") String state,
                                                                 @RequestParam(value = "from", defaultValue = "0") int from,
                                                                 @RequestParam(value = "size", defaultValue = "10") int size) throws NotFoundParameterException {
        log.info("Get booking by item owner from={}, size={}", from, size);
        BookingStatus status = BookingStatus.checkBookingStatus(state);

        Pageable pages = PageRequest.of(from / size, size);
        return bookingService.getBookingByItemOwner(userId, status, pages);
    }

    @PostMapping
    public BookingWithItemAndUserDto addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody BookingDto bookingDto) throws NotFoundParameterException {
        log.info("Add booking");
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingWithItemAndUserDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam Boolean approved) throws NotFoundParameterException {
        log.info("approve booking");
        return bookingService.approve(userId, bookingId, approved);
    }
}
