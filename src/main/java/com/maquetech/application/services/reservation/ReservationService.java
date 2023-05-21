package com.maquetech.application.services.reservation;

import com.maquetech.application.entities.reservation.ReservationEntity;
import com.maquetech.application.entities.reservation.ReservationMaterialEntity;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.reservation.SituationEnum;
import com.maquetech.application.helper.ConvertHelper;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.repositories.reservation.ReservationMaterialRepository;
import com.maquetech.application.repositories.reservation.ReservationRepository;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
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

    public List<ReservationModel> getListByUser(Date startBookingDate, Date endBookingDate, UserEntity user) {
        return parse(this.repository.getByUser(startBookingDate, endBookingDate, user.getId()));
    }

    public void cancel(Long id) {
        var reservationOptional = this.repository.findById(id);
        if (reservationOptional.isPresent()) {
            var reservation = reservationOptional.get();
            reservation.setSituation(SituationEnum.CANCELED);
            this.repository.save(reservation);
        } else {
            throw new IllegalArgumentException("Reserva não foi encontrada!");
        }
    }

    private List<ReservationModel> parse(List<ReservationEntity> entityList) {
        return entityList.stream().map(this::parse).toList();
    }

    private ReservationModel parse(ReservationEntity entity) {
        return ReservationModel
                .builder()
                .id(entity.getId())
                .message(entity.getMessage())
                .situation(entity.getSituation())
                .bookingEndDate(entity.getBookingEndDate())
                .bookingStartDate(entity.getBookingStartDate())
                .build();
    }
}
