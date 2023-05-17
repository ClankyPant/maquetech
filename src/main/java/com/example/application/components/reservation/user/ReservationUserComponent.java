package com.example.application.components.reservation.user;

import com.example.application.components.reservation.NewReservationComponent;
import com.example.application.entities.reservation.ReservationEntity;
import com.example.application.models.reservation.ReservationMaterialModel;
import com.example.application.models.reservation.ReservationModel;
import com.example.application.services.material.MaterialService;
import com.example.application.services.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;

public class ReservationUserComponent extends VerticalLayout {

    private final NewReservationComponent newReservationComponent;

    public ReservationUserComponent(MaterialService materialService, UserService userService) throws NotFoundException {
        this.newReservationComponent = new NewReservationComponent(materialService, userService);

        var gridReservation = new Grid<ReservationModel>();
        gridReservation.addColumn(ReservationModel::getBookingStartDate).setKey("start_date").setHeader("Data inicio");
        gridReservation.addColumn(ReservationModel::getBookingEndDate).setKey("end_date").setHeader("Data fim");

        setSizeFull();
        setSpacing(true);
        add(new Button("Nova reserva", event -> newReservationComponent.open()), gridReservation, newReservationComponent);
    }
}
