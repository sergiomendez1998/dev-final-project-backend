package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
  CustomerEntity findByCui(String cui);
  CustomerEntity findByNit(String nit);
  CustomerEntity findByPhoneNumber(String phoneNumber);
  CustomerEntity findByUserId(Long userId);
}
