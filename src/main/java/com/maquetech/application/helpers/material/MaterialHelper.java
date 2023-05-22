package com.maquetech.application.helpers.material;

import com.maquetech.application.entities.material.MaterialEntity;
import com.maquetech.application.models.material.MaterialModel;

import java.util.List;

public class MaterialHelper {

    private MaterialHelper() {

    }

    public static List<MaterialModel> transform(List<MaterialEntity> entityList) {
        return entityList.stream().map(MaterialHelper::transform).toList();
    }

    public static MaterialModel transform(MaterialEntity entity) {
        return MaterialModel
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .unit(entity.getUnit())
                .type(entity.getType())
                .stockQty(entity.getStockQty())
                .location(entity.getLocation())
                .stockSafeQty(entity.getStockSafeQty())
                .build();
    }
}
