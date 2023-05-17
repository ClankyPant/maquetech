package com.example.application.models.material;

import com.example.application.enums.material.MaterialTypeEnum;
import com.example.application.enums.material.MaterialUnitEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MaterialModel {

    private Long id;

    private String name;

    private String location;

    private Double stockQty;

    private Double stockSafeQty;

    private MaterialTypeEnum type;

    private MaterialUnitEnum unit;

    private boolean onReservation = false;

    public String getTypeDescription() {
        return this.type.getDescription();
    }

    public String getUnitDescription() {
        return this.unit.getDescription();
    }
}
