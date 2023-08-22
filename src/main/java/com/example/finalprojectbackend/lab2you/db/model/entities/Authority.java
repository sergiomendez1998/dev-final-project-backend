package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class Authority extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToMany(mappedBy ="authorities" )
    private List<Role> roles;
}
