package com.maquetech.application.repositories.reservation;

import com.maquetech.application.entities.reservation.ReservationMaterialEntity;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationMaterialRepository extends JpaRepository<ReservationMaterialEntity, Long> {
}
