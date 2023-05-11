package com.example.application.views.reservation;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.reservation.user.ReservationUserComponent;
import com.example.application.entities.user.UserEntity;
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

    private final UserEntity loggedUser;
    private final ReservationUserComponent reservationUserComponent = new ReservationUserComponent();

    public ReservationView(UserService userService) throws NotFoundException {
        this.loggedUser = userService.getLoggedUser();

        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        var tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Reserva", reservationUserComponent);
//                .setVisible(!loggedUser.isAdmin());
//        tabSheet.add("Cadastro", materialRegistrationComponent);

        add(tabSheet);
    }
}
