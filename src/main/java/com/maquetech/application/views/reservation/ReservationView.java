package com.maquetech.application.views.reservation;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.components.reservation.admin.ReservationAdminComponent;
import com.maquetech.application.components.reservation.user.ReservationUserComponent;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.UserHelper;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.maquetech.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import javassist.NotFoundException;

@PermitAll
@PageTitle("Reserva")
@Route(value = "", layout = MainLayout.class)
public class ReservationView extends MaqueVerticalLayout {

    private UserEntity loggedUser;

    public ReservationView(ReservationService reservationService, MaterialService materialService) throws NotFoundException {
        this.loggedUser = UserHelper.getLoggedUser();

        var tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Reserva", getReservationComponent(reservationService, materialService));

        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);
        add(tabSheet);
    }

    private Component getReservationComponent(ReservationService reservationService, MaterialService materialService) throws NotFoundException {
        if (this.loggedUser.isAdmin()) {
            return new ReservationAdminComponent(reservationService, materialService);
        }

        return new ReservationUserComponent(materialService, reservationService);
    }
}
