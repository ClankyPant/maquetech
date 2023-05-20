package com.maquetech.application.repositories.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
}
