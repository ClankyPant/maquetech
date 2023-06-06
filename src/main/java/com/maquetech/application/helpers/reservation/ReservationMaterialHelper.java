package com.maquetech.application.helpers.reservation;

import com.maquetech.application.entities.reservation.ReservationMaterialEntity;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.models.reservation.ReservationMaterialModel;

import java.util.List;

public class ReservationMaterialHelper {

    private ReservationMaterialHelper() {

    }

    public static ReservationMaterialModel transform(ReservationMaterialEntity entity) {
        var material = entity.getMaterial();

        return ReservationMaterialModel
                .builder()
                .materialModel(MaterialModel
                        .builder()
                        .id(material.getId())
                        .name(material.getName())
                        .type(material.getType())
                        .build())
                .quantity(entity.getQuantity())
                .build();
    }

    public static List<ReservationMaterialModel> transform(List<ReservationMaterialEntity> entityList) {
        return entityList.stream().map(ReservationMaterialHelper::transform).toList();
    }
}
