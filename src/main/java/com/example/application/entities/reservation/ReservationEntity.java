package com.example.application.entities.reservation;

import com.example.application.entities.AbstractBean;
import com.example.application.entities.user.UserEntity;
import com.example.application.enums.reservation.SituationEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "reservation_entity")
public class ReservationEntity extends AbstractBean {

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date bookingStartDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date bookingEndDate;

    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituationEnum situation;

    @OneToMany(mappedBy = "reservation")
    private List<MaterialReservationEntity> materialList;
}
