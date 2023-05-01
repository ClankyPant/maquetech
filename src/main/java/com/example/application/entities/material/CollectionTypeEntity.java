package com.example.application.entities.material;

import com.example.application.entities.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "collection_type_entity")
public class CollectionTypeEntity extends AbstractBean {

    @Column(nullable = false)
    private String name;
}
