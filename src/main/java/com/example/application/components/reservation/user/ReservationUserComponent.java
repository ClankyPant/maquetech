package com.example.application.components.reservation.user;

import com.example.application.entities.reservation.ReservationEntity;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ReservationUserComponent extends VerticalLayout {

    public ReservationUserComponent() {
        setSizeFull();
        setSpacing(true);

        var btnNewReservation = new Button("Nova reserva");
        var gridReservation = new Grid<ReservationEntity>();
        gridReservation.addColumn(ReservationEntity::getBookingStartDate).setKey("start_date").setHeader("Data inicio");
        gridReservation.addColumn(ReservationEntity::getBookingEndDate).setKey("end_date").setHeader("Data fim");

        add(btnNewReservation, gridReservation);
    }
}
