package com.example.application.services.material;

import com.example.application.entities.material.MaterialEntity;
import com.example.application.repositories.material.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository repository;

    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }


    public void create(MaterialEntity materialEntity) {
        this.repository.save(materialEntity);
    }

    public List<MaterialEntity> getAll() {
        return this.repository.findAll();
    }

    public MaterialEntity getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
