package com.example.finalprojectbackend.lab2you.db.repository;

import java.util.List;

public interface CRUDEntity<T> {

    T executeCreation(T entity);

    T executeUpdate(T entity);

    void executeDeleteById(Long id);

    List<T> executeReadAll();
}
