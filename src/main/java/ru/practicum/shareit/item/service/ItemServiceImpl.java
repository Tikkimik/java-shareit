package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.IncorrectParameterException;
import ru.practicum.shareit.exceptions.NotFoundParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

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

    private final ItemMapper itemMapper;

//    @Override
//    public ItemDto createItem(long userId, ItemDto itemDto) throws NotFoundParameterException {
      //  User user = userRepository.getUser(userId);

//        if (user != null) {
//            Item item = ItemMapper.toItem(userRepository.getUser(userId), itemDto);
//            return ItemMapper.toItemDto(itemRepository.createItem(item));
//        } else {
//            throw new NotFoundParameterException("user not found");
//        }
//
//    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) throws NotFoundParameterException {
        checkUserId(userId);
        checkItem(itemDto);
        itemDto.setOwner(userId);
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(itemDto)));
    }


//
//    @Override
//    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) throws NotFoundParameterException {
//        Item itemFromRepository = itemRepository.getItem(itemId);
//
//        if (itemFromRepository == null) {
//            throw new NotFoundParameterException("item not found");
//        }
//
//        Item item = ItemMapper.toItem(itemDto);
//
//        boolean userCheck = Objects.equals(itemFromRepository.getOwner().getId(), userId);
//
//        if (userCheck) {
//            if (item.getName() != null) {
//                itemFromRepository.setName(item.getName());
//            }
//
//            if (item.getDescription() != null) {
//                itemFromRepository.setDescription(item.getDescription());
//            }
//
//            if (item.getAvailable() != null) {
//                itemFromRepository.setAvailable(item.getAvailable());
//            }
//
//            if (item.getOwner() != null) {
//                itemFromRepository.setOwner(item.getOwner());
//            }
//        } else {
//            throw new NotFoundParameterException("wrong user id");
//        }
//
//        return ItemMapper.toItemDto(itemFromRepository);
//    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) throws NotFoundParameterException {

        if (!Objects.equals(getItem(userId, itemId).getOwner(), userId))
            throw new IncorrectParameterException("Exception: You must be the owner of an item to upgrade it.");

        ItemDto itemFromRepository = itemMapper.toItemDto(itemRepository.getReferenceById(itemId));

        if (itemFromRepository == null) throw new NotFoundParameterException("Exception: Item not found");

        if (itemDto.getName() != null) itemFromRepository.setName(itemDto.getName());

        if (itemDto.getDescription() != null) itemFromRepository.setDescription(itemDto.getDescription());

        if (itemDto.getAvailable() != null) itemFromRepository.setAvailable(itemDto.getAvailable());

        itemRepository.save(itemMapper.toItem(itemFromRepository));
        return itemFromRepository;

    }



//
//    @Override
//    public List<ItemDto> getAllItems(Long userId) {
//        return itemRepository.getAllItems(userId)
//                .stream()
//                .map(ItemMapper::toItemDto)
//                .collect(Collectors.toList());
//    }


    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.findAllByOwnerOrderById(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }




//
//    @Override
//    public ItemDto getItem(Long itemId) throws NotFoundParameterException {
//        Item itemFromRepository = itemRepository.getItem(itemId);
//
//        if (itemFromRepository == null) {
//            throw new NotFoundParameterException("item not found");
//        }
//
//        return ItemMapper.toItemDto(itemRepository.getItem(itemId));
//    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) throws NotFoundParameterException {
        checkItemId(itemId);
        return itemMapper.toItemDto(itemRepository.getReferenceById(userId));
    }

//
//    @Override
//    public List<ItemDto> searchAvailableItem(String text) {
//        return itemRepository.searchAvailableItem(text)
//                .stream()
//                .map(ItemMapper::toItemDto)
//                .collect(Collectors.toList());
//    }


    @Override
    public List<ItemDto> searchAvailableItem(String text) {

        if (text.isEmpty()) return null;

        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());

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

}
