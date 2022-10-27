package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingBookerDto nextBooking;
    private BookingBookerDto lastBooking;
    private List<CommentWithAuthorAndItemDto> comments;


    public static class BookingBookerDto {
        private final Long id;
        private final Long bookerId;

        private BookingBookerDto(Long id, Long bookerId) {
            this.id = id;
            this.bookerId = bookerId;
        }

        public static BookingBookerDto createBookingItemDto(Booking booking) {
            if (booking == null) return null;

            return new BookingBookerDto(booking.getId(), booking.getBooker().getId());
        }

        public Long getId() {
            return id;
        }

        public Long getBookerId() {
            return bookerId;
        }
    }
}