package com.example.finalprojectbackend.lab2you.db.repository;


import com.example.finalprojectbackend.lab2you.db.model.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findAllByIsDeletedFalse();
}
