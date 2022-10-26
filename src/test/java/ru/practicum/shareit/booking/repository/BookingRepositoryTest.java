package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Rollback(value = false)
class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ItemRepository itemRepository;

    User vasya;
    User chel;
    User masha;
    Item otvertka;
    Booking booking;
    Booking booking2;

    @BeforeEach
    void beforeEach() {
        vasya = userRepository.save(new User(null, "test1", "test1@mail.ru"));
        chel = userRepository.save(new User(null, "test2", "test2@mail.ru"));
        masha = userRepository.save(new User(null, "test3", "test3@mail.ru"));
        otvertka = itemRepository.save(new Item(null, "test", "test", true, chel, null));

        booking = bookingRepository.save(new Booking(null, LocalDateTime.now(), LocalDateTime.now(), otvertka, vasya, BookingStatus.APPROVED));

        booking2 = bookingRepository.save(new Booking(null, LocalDateTime.now(), LocalDateTime.now(), otvertka, masha, BookingStatus.APPROVED));
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void returnEmptyBookingsListByWrongBookerIdTest() {
        booking.setStart(booking.getStart().minusHours(1));
        booking.setEnd(booking.getEnd().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> results = bookingRepository
                .findBookingBookerByCurrentState(chel.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void returnEmptyBookingsListByWrongOwnerIdTest() {
        booking.setStart(booking.getStart().minusHours(1));
        booking.setEnd(booking.getEnd().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> results = bookingRepository
                .findBookingOwnerByCurrentState(vasya.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    void returnCurrentBookingsListByBookerIdTest() {
        booking.setStart(booking.getStart().minusHours(1));
        booking.setEnd(booking.getEnd().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> results = bookingRepository
                .findBookingBookerByCurrentState(vasya.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void returnCurrentBookingsListByOwnerIdTest() {
        booking.setStart(booking.getStart().minusHours(1));
        booking.setEnd(booking.getEnd().plusHours(1));
        bookingRepository.save(booking);

        List<Booking> results = bookingRepository
                .findBookingOwnerByCurrentState(chel.getId(), LocalDateTime.now(), Pageable.unpaged());

        Assertions.assertNotNull(results);
        Assertions.assertEquals(1, results.size());
    }

    @Test
    void returnNextBookingTest() {
        booking2.setStart(booking2.getStart().plusHours(1));
        booking2.setEnd(booking2.getEnd().plusHours(2));
        bookingRepository.save(booking2);

        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), chel.getId(), otvertka.getId());

        Assertions.assertNotNull(nextBooking);
        Assertions.assertEquals(nextBooking.getBooker().getName(), masha.getName());
    }

    @Test
    void returnLastBookingTest() {
        booking2.setEnd(booking2.getEnd().plusHours(1));
        bookingRepository.save(booking2);

        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), chel.getId(), otvertka.getId());

        Assertions.assertNotNull(lastBooking);
        Assertions.assertEquals(lastBooking.getBooker().getName(), vasya.getName());
    }
}