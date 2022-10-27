package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndUserDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item, BookingStatus status) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(status);
        return booking;
    }

    public static BookingWithItemAndUserDto toBookingWithItemAndUserDto(Booking booking) {
        return new BookingWithItemAndUserDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingWithItemAndUserDto.ItemInfoDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                ),
                new BookingWithItemAndUserDto.UserInfoDto(
                        booking.getBooker().getId()
                ),
                booking.getStatus()
        );
    }
}
