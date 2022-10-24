package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED,
    UNSUPPORTED_STATUS,
    ALL,
    PAST,
    CURRENT,
    FUTURE;

    public static BookingStatus checkBookingStatus(String status) {
        for (BookingStatus s : BookingStatus.values()) {
            if (s.name().equals(status))
                return s;
        }
        return UNSUPPORTED_STATUS;
    }
}
