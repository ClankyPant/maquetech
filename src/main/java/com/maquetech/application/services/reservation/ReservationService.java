package com.maquetech.application.services.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import com.maquetech.application.entities.reservation.ReservationMaterialEntity;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.reservation.SituationEnum;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.repositories.reservation.ReservationMaterialRepository;
import com.maquetech.application.repositories.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final ReservationMaterialRepository reservationMaterialRepository;

    public ReservationService(ReservationRepository repository, ReservationMaterialRepository reservationMaterialRepository) {
        this.repository = repository;
        this.reservationMaterialRepository = reservationMaterialRepository;
    }

    public void create(List<MaterialModel> materialModelList, ReservationModel reservationModel, UserEntity user) {
        var reservation = new ReservationEntity();
        reservation.setBookingStartDate(reservationModel.getBookingStartDate());
        reservation.setBookingEndDate(reservationModel.getBookingStartDate());
        reservation.setUser(user);
        reservation.setSituation(SituationEnum.PENDING);
        reservation.setMaterialList(new ArrayList<>());
        reservation = repository.save(reservation);

        for (var materialModel : materialModelList) {
            var reservationMaterial = new ReservationMaterialEntity();
            reservationMaterial.setReservation(reservation);
            reservationMaterial.setMaterial(materialModel.getEntidade());
            reservationMaterial.setQuantity(materialModel.getReservationQuantity());
            reservation.getMaterialList().add(reservationMaterial);
        }

        repository.save(reservation);
    }
}
