package com.maquetech.application.components.reservation.user;

import com.maquetech.application.components.reservation.NewReservationComponent;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;

public class ReservationUserComponent extends VerticalLayout {

    private final NewReservationComponent newReservationComponent;

    public ReservationUserComponent(MaterialService materialService, UserService userService, ReservationService reservationService) throws NotFoundException {
        this.newReservationComponent = new NewReservationComponent(materialService, userService, reservationService);

        var gridReservation = new Grid<ReservationModel>();
        gridReservation.addColumn(ReservationModel::getBookingStartDate).setKey("start_date").setHeader("Data inicio");
        gridReservation.addColumn(ReservationModel::getBookingEndDate).setKey("end_date").setHeader("Data fim");

        setSizeFull();
        setSpacing(true);
        add(new Button("Nova reserva", event -> newReservationComponent.open()), gridReservation, newReservationComponent);
    }
}
