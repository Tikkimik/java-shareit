package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.CreatingException;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws NotFoundParameterException {
        checkUserId(userId);
        checkItem(itemDto);
        itemDto.setOwner(userId);
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws NotFoundParameterException {

        if (!Objects.equals(getItem(userId, itemId).getOwner(), userId))
            throw new NotFoundParameterException("Exception: You must be the owner of an item to upgrade it.");

        ItemDto itemFromRepository = itemMapper.toItemDto(itemRepository.getReferenceById(itemId));

        if (itemFromRepository == null) throw new NotFoundParameterException("Exception: Item not found");

        if (itemDto.getName() != null) itemFromRepository.setName(itemDto.getName());

        if (itemDto.getDescription() != null) itemFromRepository.setDescription(itemDto.getDescription());

        if (itemDto.getAvailable() != null) itemFromRepository.setAvailable(itemDto.getAvailable());

        itemRepository.save(itemMapper.toItem(itemFromRepository));
        return itemFromRepository;
    }

    @Override
    public List<ItemWithBookingDto> getAllByUserId(Long userId) {
        return itemRepository.findAllByOwnerOrderById(userId)
                .stream()
                .map(item -> setBookingsForItem(item, userId))
                .map(this::addCommentsForItem)
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingDto getItem(Long userId, Long itemId) throws NotFoundParameterException {
        checkItemId(itemId);
        return addCommentsForItem(setBookingsForItem(itemRepository.getReferenceById(itemId), userId));
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {

        if (text.isEmpty()) return new ArrayList<>();

        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());

    }

//    @Override
//    public List<ItemDto> searchAvailableItem(String text) {
//        if (text.isEmpty()) return null;
//
//        return itemRepository.findAll().stream()
//                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
//                .filter(item -> item.getAvailable().equals(true))
//                .map(itemMapper::toItemDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public CommentWithAuthorAndItemDto addComment(Long userId, Long itemId, CommentDto commentDto) throws CreatingException {
        if (commentDto.getText().isEmpty()) throw new CreatingException("Exception: Comment is empty.");

        List<Booking> bookings = bookingRepository.findAllByItemIdAndBookerIdAndStartBefore(
                itemId, userId, LocalDateTime.now()
        );

        if (bookings.size() < 1) throw new CreatingException("Exception: Can't comment on it.");

        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        commentDto.setCreated(LocalDate.now());

        CommentWithAuthorAndItemDto comment = commentMapper.toCommentWithAuthorAndItemDto(
                commentRepository.save(commentMapper.toComment(commentDto))
        );

        comment.setAuthorName(userRepository.getReferenceById(commentDto.getAuthorId()).getName());
        return comment;
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemRepository.deleteById(itemId);
    }

    private void checkItem(ItemDto itemDto) {

        if (itemDto.getName().isEmpty())
            throw new IncorrectParameterException("Exception: Item name cannot be null.");

        if (itemDto.getDescription() == null || Objects.equals(itemDto.getDescription(), ""))
            throw new IncorrectParameterException("Exception: Item description cannot be empty.");

        if (!itemDto.getAvailable())
            throw new IncorrectParameterException("Exception: Item cannot be unavailable.");

    }

    private void checkItemId(Long id) throws NotFoundParameterException {

        if (!itemRepository.existsById(id))
            throw new NotFoundParameterException("Exception: Wrong item id.");

    }

    private void checkUserId(Long id) throws NotFoundParameterException {

        if (!userRepository.existsById(id))
            throw new NotFoundParameterException("Exception: Wrong user id.");

    }

    private ItemWithBookingDto setBookingsForItem(Item item, Long userId) {

        ItemWithBookingDto itemDtoWithBooking = itemMapper.toItemWithBookingDto(item);

        List<Booking> lastBookings = bookingRepository.findAllByItemIdAndStartIsBeforeOrderByStartDesc(
                        item.getId(), LocalDateTime.now())
                .stream()
                .filter(booking -> !Objects.equals(booking.getBookerId(), userId))
                .collect(Collectors.toList());

        if (lastBookings.size() != 0)
            itemDtoWithBooking.setLastBooking(bookingMapper.toBookingDto(lastBookings.get(0)));

        List<Booking> nextBookings = bookingRepository.findAllByItemIdAndStartIsAfterOrderByStartAsc(
                        item.getId(), LocalDateTime.now())
                .stream()
                .filter(booking -> !Objects.equals(booking.getBookerId(), userId))
                .collect(Collectors.toList());

        if (nextBookings.size() != 0)
            itemDtoWithBooking.setNextBooking(bookingMapper.toBookingDto(nextBookings.get(0)));

        return itemDtoWithBooking;

    }

    private ItemWithBookingDto addCommentsForItem(ItemWithBookingDto item) {
//        List<CommentDtoWithAuthorAndItem> commentDtoWithAuthorAndItemList = commentRepository.findAllByItem(item.getId())
//                .stream().

        List<CommentWithAuthorAndItemDto> commentWithAuthorAndItemListDto = new ArrayList<>();

        for (Comment comment : commentRepository.findAllByItem(item.getId())) {
            CommentWithAuthorAndItemDto commentWithAuthorAndItemDto = commentMapper.toCommentWithAuthorAndItemDto(comment);
            commentWithAuthorAndItemDto.setAuthorName(userRepository.getReferenceById(comment.getAuthor()).getName());
            commentWithAuthorAndItemListDto.add(commentWithAuthorAndItemDto);
        }

        item.setComments(commentWithAuthorAndItemListDto);
        return item;
    }

}
