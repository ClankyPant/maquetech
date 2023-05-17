package com.example.application.components.reservation.user;

import com.example.application.components.reservation.NewReservationComponent;
import com.example.application.entities.reservation.ReservationEntity;
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

        var btnNewReservation = new Button("Nova reserva");
        btnNewReservation.addClickListener(event -> newReservationComponent.open());

        var gridReservation = new Grid<ReservationEntity>();
        gridReservation.addColumn(ReservationEntity::getBookingStartDate).setKey("start_date").setHeader("Data inicio");
        gridReservation.addColumn(ReservationEntity::getBookingEndDate).setKey("end_date").setHeader("Data fim");

        setSizeFull();
        setSpacing(true);
        add(btnNewReservation, gridReservation, newReservationComponent);
    }
}
