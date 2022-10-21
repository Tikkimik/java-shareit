package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingByStartAndEndAndBookerIdAndItemId(LocalDateTime start, LocalDateTime end, Long userId, Long itemId);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findAllByItemIdAndStartIsAfterOrderByStartAsc(Long itemId, LocalDateTime start);

    List<Booking> findAllByItemIdAndStartIsBeforeOrderByStartDesc(Long itemId, LocalDateTime start);

    List<Booking> findAllByItemIdAndBookerIdAndStartBefore(Long itemId, Long bookerId, LocalDateTime start);

}
