package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exceptions.IncorrectStatusException;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    ALL,
    PAST,
    CURRENT,
    FUTURE;

    public static BookingStatus checkBookingStatus(String status) {
        for (BookingStatus s : BookingStatus.values()) {
            if (s.name().equals(status))
                return s;
        }
        throw new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS");
    }
}
