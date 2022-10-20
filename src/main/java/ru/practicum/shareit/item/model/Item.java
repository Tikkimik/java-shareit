package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_description", nullable = false)
    private String description;

    @Column(name = "item_is_available", nullable = false)
    private Boolean available;

    @Column(name = "item_request_id", nullable = false)
    private Long request;

    @Column(name = "item_owner_id", nullable = false)
    private Long owner;

//    public Long getOwner() {
//        return owner;
//    }
//
//    public Item(Long id, String name, String description, Boolean available, Long owner) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.available = available;
//        this.owner = owner;
//    }
}
