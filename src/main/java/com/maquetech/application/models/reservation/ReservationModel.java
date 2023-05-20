package com.maquetech.application.models.reservation;

import com.maquetech.application.enums.reservation.SituationEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReservationModel {

    private Date bookingStartDate;

    private Date bookingEndDate;

    private SituationEnum situation;

    private List<ReservationMaterialModel> materialList;
}
