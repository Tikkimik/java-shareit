package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT item " +
            "FROM Item item " +
            "WHERE (LOWER(item.name) LIKE %:text% " +
            "OR LOWER(item.description) LIKE %:text%) " +
            "AND item.available = true")
    List<Item> search(String text);


    @Query("SELECT item " +
            "FROM Item item " +
            "WHERE item.owner.id = :userId " +
            "ORDER BY item.id")
    List<Item> findAllById(Long userId);
}
