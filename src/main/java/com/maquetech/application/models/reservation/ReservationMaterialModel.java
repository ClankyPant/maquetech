package com.maquetech.application.models.reservation;

import com.maquetech.application.models.material.MaterialModel;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMaterialModel {

    private Double quantity;

    private MaterialModel materialModel;

    public String getMaterialName() {
        return this.materialModel != null ? this.materialModel.getName() : null;
    }

    public Long getMaterialId() {
        return this.materialModel != null ? this.materialModel.getId() : null;
    }
}

