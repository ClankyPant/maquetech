package com.maquetech.application.repositories.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query(value = """
            
            SELECT me.id AS material_id,
                   SUM(rme.quantity) reserved_qty
            FROM reservation_entity re
            JOIN reservation_material_entity rme ON rme.reservation_id = re.id
            JOIN material_entity me ON me.id = rme.material_id
            WHERE re.situation NOT IN ('FINISHED', 'CANCELED')
            AND (re.booking_start_date >= :r_ini AND re.booking_end_date <= :r_fim
                OR re.booking_start_date <= :r_ini AND re.booking_end_date >= :r_ini)
            GROUP BY me.id
            
            """, nativeQuery = true)
    List<Object[]> getOnReservation(@Param("r_ini") Date startBookingDate, @Param("r_fim") Date endBookingDate);
}
