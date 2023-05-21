package com.maquetech.application.entities.material;

import com.maquetech.application.entities.AbstractBean;
import com.maquetech.application.enums.material.MaterialTypeEnum;
import com.maquetech.application.enums.material.MaterialUnitEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "material_entity")
public class MaterialEntity extends AbstractBean {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double stockQty;

    private Double stockSafeQty;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialUnitEnum unit;

    @OneToOne
    private CollectionTypeEntity collectionType;

    private Boolean onlyProfessor;

    public boolean isConsumable() {
        return this.type.equals(MaterialTypeEnum.CONSUMABLE);
    }
}
