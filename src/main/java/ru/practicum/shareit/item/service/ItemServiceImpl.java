package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

//    @Autowired
//    private UserRepository userRepository;

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) throws NotFoundParameterException {
      //  User user = userRepository.getUser(userId);

//        if (user != null) {
//            Item item = ItemMapper.toItem(userRepository.getUser(userId), itemDto);
//            return ItemMapper.toItemDto(itemRepository.createItem(item));
//        } else {
//            throw new NotFoundParameterException("user not found");
//        }
        return null;
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) throws NotFoundParameterException {
        Item itemFromRepository = itemRepository.getItem(itemId);

        if (itemFromRepository == null) {
            throw new NotFoundParameterException("item not found");
        }

        Item item = ItemMapper.toItem(itemDto);

        boolean userCheck = Objects.equals(itemFromRepository.getOwner().getId(), userId);

        if (userCheck) {
            if (item.getName() != null) {
                itemFromRepository.setName(item.getName());
            }

            if (item.getDescription() != null) {
                itemFromRepository.setDescription(item.getDescription());
            }

            if (item.getAvailable() != null) {
                itemFromRepository.setAvailable(item.getAvailable());
            }

            if (item.getOwner() != null) {
                itemFromRepository.setOwner(item.getOwner());
            }
        } else {
            throw new NotFoundParameterException("wrong user id");
        }

        return ItemMapper.toItemDto(itemFromRepository);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.getAllItems(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(Long itemId) throws NotFoundParameterException {
        Item itemFromRepository = itemRepository.getItem(itemId);

        if (itemFromRepository == null) {
            throw new NotFoundParameterException("item not found");
        }

        return ItemMapper.toItemDto(itemRepository.getItem(itemId));
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        return itemRepository.searchAvailableItem(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
