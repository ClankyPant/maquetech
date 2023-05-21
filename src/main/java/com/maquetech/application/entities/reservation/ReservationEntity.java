package com.maquetech.application.entities.reservation;

import com.maquetech.application.entities.AbstractBean;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.reservation.SituationEnum;
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingStartDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingEndDate;

    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituationEnum situation;

    @Column(columnDefinition = "text")
    private String message;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReservationMaterialEntity> materialList;
}
