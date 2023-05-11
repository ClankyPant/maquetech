package com.example.application.components.reservation.user;

import com.example.application.components.reservation.NewReservationComponent;
import com.example.application.entities.reservation.ReservationEntity;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ReservationUserComponent extends VerticalLayout {

    private final NewReservationComponent newReservationComponent = new NewReservationComponent();

    public ReservationUserComponent() {
        setSizeFull();
        setSpacing(true);

        var btnNewReservation = new Button("Nova reserva");
        btnNewReservation.addClickListener(event -> newReservationComponent.open());

        var gridReservation = new Grid<ReservationEntity>();
        gridReservation.addColumn(ReservationEntity::getBookingStartDate).setKey("start_date").setHeader("Data inicio");
        gridReservation.addColumn(ReservationEntity::getBookingEndDate).setKey("end_date").setHeader("Data fim");

        add(btnNewReservation, gridReservation, newReservationComponent);
    }
}
