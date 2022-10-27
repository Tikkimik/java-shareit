package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingWithItemAndUserDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemInfoDto item;
    private UserInfoDto booker;
    private BookingStatus status;

    public static class UserInfoDto {
        private final Long id;

        public UserInfoDto(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    public static class ItemInfoDto {
        private final Long id;
        private final String name;

        public ItemInfoDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
