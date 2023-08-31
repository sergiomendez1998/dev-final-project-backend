package com.example.finalprojectbackend.lab2you.db.repository;


import com.example.finalprojectbackend.lab2you.db.model.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
    List<Item> findAllByIsActiveTrue();
}
