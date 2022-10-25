package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsBookingByItemIdAndBookerIdAndEndBefore(Long itemId, Long userId, LocalDateTime now);

    @Query("SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.booker.id = :userId " +
            "AND booking.start < :now " +
            "AND booking.end > :now " +
            "ORDER BY booking.start DESC")
    List<Booking> findBookingBookerByCurrentState(Long userId, LocalDateTime now, Pageable pages);

    @Query("SELECT booking " +
            "FROM Booking booking " +
            "WHERE booking.item.owner.id = :userId " +
            "AND booking.start < :now " +
            "AND booking.end > :now " +
            "ORDER BY booking.start DESC")
    List<Booking> findBookingOwnerByCurrentState(Long userId, LocalDateTime now, Pageable pages);

    @Query("SELECT booking " +
            "FROM  Booking booking " +
            "WHERE booking.item.id = :itemId " +
            "AND booking.item.owner.id = :userId " +
            "AND booking.status = 'APPROVED' " +
            "AND booking.start > :now " +
            "ORDER BY booking.start")
    Booking findNextBooking(LocalDateTime now, Long userId, Long itemId);

    @Query("SELECT booking " +
            "FROM  Booking booking " +
            "WHERE booking.item.id = :itemId " +
            "AND booking.item.owner.id = :userId " +
            "AND booking.status = 'APPROVED' " +
            "AND booking.end < :now " +
            "ORDER BY booking.start")
    Booking findLastBooking(LocalDateTime now, Long userId, Long itemId);

    List<Booking> findBookingsByBookerIdOrderByStartDesc(Long userId, Pageable pages);

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long userId, Pageable pages);

    List<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now, Pageable pages);

    List<Booking> findBookingsByBookerIdAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now, Pageable pages);

    List<Booking> findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now, Pageable pages);

    List<Booking> findBookingsByItemOwnerIdAndEndAfterOrderByStartDesc(Long userId, LocalDateTime now, Pageable pages);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus state, Pageable pages);

    List<Booking> findBookingsByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus state, Pageable pages);

}
