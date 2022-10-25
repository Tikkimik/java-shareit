package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentWithAuthorAndItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.model.CommentMapper.toComment;
import static ru.practicum.shareit.item.model.CommentMapper.toCommentWithAuthorAndItemDto;
import static ru.practicum.shareit.item.model.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws NotFoundParameterException {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong item id."));

        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(()
                    -> new NotFoundParameterException("Exception: Request not found."));
            return ItemMapper.toItemDto(itemRepository.save(toItem(itemDto, owner, request)));
        }

        return ItemMapper.toItemDto(itemRepository.save(toItem(itemDto, owner)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws NotFoundParameterException {
        if (!userRepository.existsById(userId))
            throw new NotFoundParameterException("Exception: Wrong user id.");

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundParameterException("Exception: Wrong item id."));

        if (!item.getOwner().getId().equals(userId))
            throw new NotFoundParameterException("Exception: You must be the owner of an item to upgrade it.");

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public List<ItemWithBookingDto> getAllByUserId(Long userId) {
        return itemRepository.findAllById(userId)
                .stream()
                .map(item -> toItemWithBookingDto(
                                item,
                                bookingRepository.findNextBooking(LocalDateTime.now(), userId, item.getId()),
                                bookingRepository.findLastBooking(LocalDateTime.now(), userId, item.getId()),
                                commentRepository.findCommentsByItemId(item.getId())
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingDto getItem(Long userId, Long itemId) throws NotFoundParameterException {
        if (!userRepository.existsById(userId))
            throw new NotFoundParameterException("Exception: Wrong user id.");

        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundParameterException("Exception: Wrong item id."));

        Booking lastBooking = bookingRepository.findLastBooking(LocalDateTime.now(), userId, itemId);
        Booking nextBooking = bookingRepository.findNextBooking(LocalDateTime.now(), userId, itemId);
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        return toItemWithBookingDto(item, nextBooking, lastBooking, comments);
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {

        if (text.isBlank()) return new ArrayList<>();

        return itemRepository.search(text.toLowerCase())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentWithAuthorAndItemDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IncorrectParameterException("Exception: Wrong user id."));

        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new IncorrectParameterException("Exception: Wrong item id."));

        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now()))
            throw new IncorrectParameterException("The user has not used this item yet");

        if (Objects.equals(commentDto.getText(), ""))
            throw new IncorrectParameterException("Exception: Comment cannot be empty");

        Comment comment = toComment(commentDto, user, item);
        return toCommentWithAuthorAndItemDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
