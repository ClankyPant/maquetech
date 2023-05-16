package com.example.application.repositories.material;

import com.example.application.entities.material.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {

    List<MaterialEntity> onlyProfessorIsFalse();

}
