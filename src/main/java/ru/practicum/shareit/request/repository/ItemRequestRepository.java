package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestsByRequesterId(Long userId);

    @Query(value = "SELECT request " +
            "FROM ItemRequest request " +
            "WHERE request.requester.id <> :userId")
    List<ItemRequest> findRequests(Long userId, Pageable pageable);
}
