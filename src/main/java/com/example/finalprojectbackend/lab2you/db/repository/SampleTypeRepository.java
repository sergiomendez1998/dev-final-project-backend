package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.SampleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SampleTypeRepository extends JpaRepository<SampleTypeEntity, Long> {
    List<SampleTypeEntity> findAllByIsDeletedFalse();
}
