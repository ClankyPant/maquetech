package com.example.application.models.reservation;

import com.example.application.entities.material.MaterialEntity;
import com.example.application.entities.reservation.ReservationEntity;
import com.example.application.models.material.MaterialModel;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

public class ReservationMaterialModel {

    private Double quantity;

    private MaterialModel material;
}
