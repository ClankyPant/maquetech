package com.maquetech.application.repositories.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import com.maquetech.application.models.reservation.ReservationModel;
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
            WHERE (me."type" <> 'CONSUMABLE' AND re.situation NOT IN ('FINISHED', 'CANCELED') OR me."type" = 'CONSUMABLE' AND re.situation NOT IN ('FINISHED', 'CANCELED', 'IN_PROGRESS'))
            AND (re.booking_start_date >= :r_ini AND re.booking_end_date <= :r_fim
                OR re.booking_start_date <= :r_ini AND re.booking_end_date >= :r_ini)
            GROUP BY me.id
            
            """, nativeQuery = true)
    List<Object[]> getOnReservation(@Param("r_ini") Date startBookingDate, @Param("r_fim") Date endBookingDate);

    @Query(value = """
                SELECT re
                FROM reservation_entity re
                WHERE (re.bookingStartDate >= :r_ini OR CAST(:r_ini AS date) IS NULL)
                AND (re.bookingEndDate <= :r_fim OR CAST(:r_fim AS date) IS NULL)
                AND (re.user.id = :user_id OR :user_id IS NULL)
            """)
    List<ReservationEntity> getByUser(@Param("r_ini") Date startBookingDate, @Param("r_fim") Date endBookingDate, @Param("user_id") Long userId);
}
