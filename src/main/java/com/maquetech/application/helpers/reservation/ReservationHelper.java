package com.maquetech.application.helpers.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import com.maquetech.application.models.reservation.ReservationModel;

import java.util.List;

public class ReservationHelper {

    private ReservationHelper() {}

    public static List<ReservationModel> transform(List<ReservationEntity> entityList) {
        return entityList.stream().map(ReservationHelper::transform).toList();
    }

    static ReservationModel transform(ReservationEntity entity) {
        return ReservationModel
                .builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .situation(entity.getSituation())
                .userName(entity.getUser().getName())
                .bookingEndDate(entity.getBookingEndDate())
                .materialList(ReservationMaterialHelper.transform(entity.getMaterialList()))
                .bookingStartDate(entity.getBookingStartDate())
                .build();
    }
}
