package com.maquetech.application.models.reservation;

import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.models.material.MaterialModel;
import jakarta.persistence.Convert;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMaterialModel {

    private Double quantity;

    private Double damageQuantity;

    private MaterialModel materialModel;

    public String getMaterialName() {
        return this.materialModel != null ? this.materialModel.getName() : null;
    }

    public Long getMaterialId() {
        return this.materialModel != null ? this.materialModel.getId() : null;
    }

    public Double getDamageQuantity() {
        return ConvertHelper.getDouble(this.damageQuantity, 0D);
    }
}

