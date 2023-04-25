package com.example.application.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class AbstractBean {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
