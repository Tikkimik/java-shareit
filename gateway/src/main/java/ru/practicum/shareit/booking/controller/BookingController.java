package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.IncorrectStatusException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId) throws NotFoundParameterException, CreatingException {
        log.info("Get extended booking information");
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState status = BookingState.from(state)
                .orElseThrow(() -> new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS"));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingByBooker(userId, status, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(required = false, defaultValue = "ALL") String state,
                                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) throws NotFoundParameterException {
        log.info("Get booking by item owner from={}, size={}", from, size);
        BookingState status = BookingState.from(state).orElseThrow(() ->
                new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS"));

        return bookingClient.getBookingByItemOwner(userId, status, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid BookingDto bookingDto) {
        log.info("Creating booking {}, userId {}", bookingDto, userId);
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam Boolean approved) {
        log.info("Approve booking - {} by userId - {} approve - {}", bookingId, userId, approved);
        return bookingClient.approve(userId, bookingId, approved);
    }

}
