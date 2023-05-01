package com.example.application.services.material;

import com.example.application.entities.material.CollectionTypeEntity;
import com.example.application.repositories.material.CollectionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionTypeService {

    private final CollectionTypeRepository collectionTypeRepository;

    public CollectionTypeService(CollectionTypeRepository collectionTypeRepository) {
        this.collectionTypeRepository = collectionTypeRepository;
    }

    public void create(CollectionTypeEntity collectionTypeEntity) {
        this.collectionTypeRepository.save(collectionTypeEntity);
    }

    public List<CollectionTypeEntity> getAll() {
        return this.collectionTypeRepository.findAll();
    }
}
