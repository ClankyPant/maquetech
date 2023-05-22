package com.maquetech.application.services.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import com.maquetech.application.entities.reservation.ReservationMaterialEntity;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.reservation.SituationEnum;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.helpers.reservation.ReservationHelper;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.models.reservation.ReservationMaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.repositories.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public void create(List<MaterialModel> materialModelList, ReservationModel reservationModel, UserEntity user) {
        var reservation = new ReservationEntity();
        reservation.setBookingStartDate(reservationModel.getBookingStartDate());
        reservation.setBookingEndDate(reservationModel.getBookingEndDate());
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

    public Map<Long, Double> getOnReservationMap(Date startBookingDate, Date endBookingDate) {
        var result = repository.getOnReservation(startBookingDate, endBookingDate);

        return result.stream()
                        .collect(Collectors.groupingBy(data -> ConvertHelper.getLong(data[0], -1L),
                                Collectors.summingDouble(data -> ConvertHelper.getDouble(data[1], 0D))));
    }

    public List<ReservationModel> getListByUser(Date startBookingDate, Date endBookingDate, Long userId, SituationEnum situation) {
        return ReservationHelper.transform(this.repository.getByUser(startBookingDate, endBookingDate, userId, situation));
    }

    public ReservationModel receive(Long id, ReservationModel reservationModel) {
        return updateSituation(id, SituationEnum.FINISHED, reservationModel);
    }

    public ReservationModel deliver(Long id, ReservationModel reservationModel) {
        return updateSituation(id, SituationEnum.IN_PROGRESS, reservationModel);
    }

    public ReservationModel approve(Long id, ReservationModel reservationModel) {
        return updateSituation(id, SituationEnum.APPROVED, reservationModel);
    }

    public ReservationModel cancel(Long id, ReservationModel reservationModel) {
        return updateSituation(id, SituationEnum.CANCELED, reservationModel);
    }

    private ReservationModel updateSituation(Long id, SituationEnum situation, ReservationModel reservationModel) {
        var reservation = this.getById(id);
        reservation.setSituation(situation);

        if (reservationModel != null) {
            reservationModel.setSituation(situation);
            reservation.setMessage(reservationModel.getMessage());
        }

        this.repository.save(reservation);

        return reservationModel;
    }

    public ReservationEntity getById(Long id) {
        var reservationOptional = this.repository.findById(id);
        if (reservationOptional.isEmpty()) {
            throw new IllegalArgumentException("Reserva não foi encontrada!");
        }

        return reservationOptional.get();
    }
}
