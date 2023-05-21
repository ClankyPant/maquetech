package com.maquetech.application.views.reservation;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.components.reservation.user.ReservationUserComponent;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.maquetech.application.services.user.UserService;
import com.maquetech.application.views.MainLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import javassist.NotFoundException;

@PermitAll
@PageTitle("Reserva")
@Route(value = "", layout = MainLayout.class)
public class ReservationView extends MaqueVerticalLayout {

    private final ReservationUserComponent reservationUserComponent;

    public ReservationView(ReservationService reservationService, MaterialService materialService, UserService userService) throws NotFoundException {
        this.reservationUserComponent = new ReservationUserComponent(materialService, userService, reservationService);

        var tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Reserva", reservationUserComponent);

        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);
        add(tabSheet);
    }
}
