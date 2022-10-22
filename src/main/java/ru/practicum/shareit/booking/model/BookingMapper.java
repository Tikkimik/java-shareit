package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;

@Component
public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getItemId(), bookingDto.getBookerId(), bookingDto.getStatus());
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getItemId(), booking.getBookerId(),  booking.getStatus());
    }

    public static BookingWithItemAndUserDto toBookingWithItemAndUserDto(BookingDto bookingDto) {
        return new BookingWithItemAndUserDto(bookingDto.getId(), bookingDto.getStart(),
                bookingDto.getEnd(), bookingDto.getStatus());
    }
}
