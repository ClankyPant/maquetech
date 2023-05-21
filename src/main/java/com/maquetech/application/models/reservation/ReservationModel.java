package com.maquetech.application.models.reservation;

import com.maquetech.application.enums.reservation.SituationEnum;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationModel {

    private Long id;

    private Date bookingStartDate;

    private Date bookingEndDate;

    private SituationEnum situation;

    private List<ReservationMaterialModel> materialList;
}
