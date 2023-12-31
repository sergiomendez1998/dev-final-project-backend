package com.example.finalprojectbackend.lab2you.db.repository;


import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {
    EmployeeEntity findByCui(String cui);
    EmployeeEntity findByUserId(Long roleId);
    EmployeeEntity findByPhoneNumber(String phoneNumber);

}
