package com.example.application.entities.reservation;

import com.example.application.entities.AbstractBean;
import com.example.application.entities.material.MaterialEntity;
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
