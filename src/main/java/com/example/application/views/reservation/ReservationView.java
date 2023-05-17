package com.example.application.views.reservation;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.reservation.user.ReservationUserComponent;
import com.example.application.entities.user.UserEntity;
import com.example.application.services.material.MaterialService;
import com.example.application.services.user.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import javassist.NotFoundException;

@PermitAll
@PageTitle("Reservar")
@Route(value = "reservation", layout = MainLayout.class)
public class ReservationView extends MaqueVerticalLayout {

    private final ReservationUserComponent reservationUserComponent;

    public ReservationView(MaterialService materialService, UserService userService) throws NotFoundException {
        this.reservationUserComponent = new ReservationUserComponent(materialService, userService);

        var tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Reserva", reservationUserComponent);

        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);
        add(tabSheet);
    }
}
