package com.maquetech.application.entities.reservation;

import com.maquetech.application.entities.AbstractBean;
import com.maquetech.application.entities.material.MaterialEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "reservation_material_entity")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "reservation_id", "material_id" }) })
public class ReservationMaterialEntity extends AbstractBean {

    @OneToOne
    private ReservationEntity reservation;

    @ManyToOne
    private MaterialEntity material;

    private Double quantity;
}
