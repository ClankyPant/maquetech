package com.example.application.entities.reservation;

import com.example.application.entities.AbstractBean;
import com.example.application.entities.material.MaterialEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "material_reservation_entity")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "reservation_id", "material_id" }) })
public class MaterialReservationEntity extends AbstractBean {

    @OneToOne
    private ReservationEntity reservation;

    @ManyToOne
    private MaterialEntity material;

    private Double quantity;
}
