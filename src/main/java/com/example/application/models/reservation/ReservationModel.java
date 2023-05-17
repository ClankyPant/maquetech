package com.example.application.models.reservation;

import com.example.application.entities.reservation.ReservationMaterialEntity;
import com.example.application.entities.user.UserEntity;
import com.example.application.enums.reservation.SituationEnum;
import jakarta.persistence.*;
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
