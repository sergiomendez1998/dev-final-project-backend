package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.SupportTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTypeRepository extends JpaRepository<SupportTypeEntity, Long> {
    List<SupportTypeEntity> findAllByIsDeletedFalse();
}
