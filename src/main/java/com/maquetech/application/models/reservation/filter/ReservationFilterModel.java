package com.maquetech.application.models.reservation.filter;

import com.maquetech.application.enums.reservation.SituationEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReservationFilterModel {

    private Date bookingEndDate;
    private Date bookingStartDate;
    private SituationEnum situationEnum;

}
