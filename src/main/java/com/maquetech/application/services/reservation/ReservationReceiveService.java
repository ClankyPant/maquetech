package com.maquetech.application.services.reservation;

import com.maquetech.application.models.reservation.ReservationMaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.services.material.MaterialService;
import org.springframework.stereotype.Service;

@Service
public class ReservationReceiveService {

    private final MaterialService materialService;

    public ReservationReceiveService(MaterialService materialService) {
        this.materialService = materialService;
    }

    public void reduceReservationQuantity(ReservationModel reservationModel) {
        var materialMap = materialService.getMapById(reservationModel.getReservationMaterialList().stream().map(ReservationMaterialModel::getMaterialId).toList());
        for (var material : reservationModel.getReservationMaterialList()) {
            var materialId = material.getMaterialId();
            materialMap.get(materialId).setStockQty(materialMap.get(materialId).getStockQty() - material.getDamageQuantity());
        }

        materialService.save(materialMap.values().stream().toList());
    }
}
