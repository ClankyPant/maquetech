package com.example.application.services.material;

import com.example.application.entities.material.MaterialEntity;
import com.example.application.repositories.material.MaterialRepository;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private final MaterialRepository repository;

    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }


    public void create(MaterialEntity materialEntity) {
        this.repository.save(materialEntity);
    }
}
