package com.maquetech.application.components.reservation.user;

import com.maquetech.application.components.reservation.NewReservationComponent;
import com.maquetech.application.converters.ConvertLocalDateTimeToDate;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helper.LocalDateTimeHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.models.reservation.filter.ReservationFilterModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import javassist.NotFoundException;

import java.time.Duration;

public class ReservationUserComponent extends VerticalLayout {

    private final UserEntity user;
    private final ReservationService reservationService;
    private final NewReservationComponent newReservationComponent;
    private final Grid<ReservationModel> grid = new Grid<>();
    private final Binder<ReservationFilterModel> binder = new Binder<>();

    public ReservationUserComponent(MaterialService materialService, UserService userService, ReservationService reservationService) throws NotFoundException {
        this.newReservationComponent = new NewReservationComponent(materialService, userService, reservationService);
        this.reservationService = reservationService;
        this.user = userService.getLoggedUser();

        init();
    }

    private void init() {
        grid.addColumn(ReservationModel::getId).setKey("id").setHeader("Código").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(ReservationModel::getBookingStartDate).setKey("start_date").setHeader("Data inicio").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(ReservationModel::getBookingEndDate).setKey("end_date").setHeader("Data fim").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(ReservationModel::getSituation).setKey("situation").setHeader("Situação").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(this::getActionButton).setKey("action_button").setHeader("Ações").setTextAlign(ColumnTextAlign.CENTER);

        setSizeFull();
        setSpacing(true);
        add(
                getHeader(),
                grid,
                newReservationComponent
        );
    }

    private Component getActionButton(ReservationModel reservationModel) {
        var btnVerify = new Button("Verificar");
        btnVerify.addClickListener(event -> {

        });

        var btnCancel = new Button("Cancelar");
        btnCancel.addClickListener(event -> {

        });

        var hlContent = new HorizontalLayout();
        hlContent.add(btnVerify, btnCancel);
        hlContent.setJustifyContentMode(JustifyContentMode.CENTER);
        
        return hlContent;
    }

    private Component getHeader() {
        var startDateTimePicker = new DateTimePicker();
        startDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        startDateTimePicker.setValue(LocalDateTimeHelper.getNowPlus15Minutes());
        startDateTimePicker.setStep(Duration.ofMinutes(15));
        binder.forField(startDateTimePicker)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingStartDate, ReservationFilterModel::setBookingStartDate);

        var endDateTimePicket = new DateTimePicker();
        endDateTimePicket.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        endDateTimePicket.setValue(LocalDateTimeHelper.getNowPlus1HourAnd15Minutes());
        endDateTimePicket.setStep(Duration.ofMinutes(15));
        binder.forField(endDateTimePicket)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingEndDate, ReservationFilterModel::setBookingEndDate);

        var btnConsult = new Button("Consultar");
        btnConsult.addClickListener(event -> loadGrid());

        var formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0px", 1),
                new FormLayout.ResponsiveStep("1000px", 4)
        );
        formLayout.add(
                startDateTimePicker,
                endDateTimePicket,
                btnConsult,
                new Button("Nova reserva", event -> newReservationComponent.open())
        );

        return formLayout;
    }

    private void loadGrid() {
        try {
            var reservationFilter = new ReservationFilterModel();
            binder.writeBean(reservationFilter);

            this.grid.setItems(
                    this.reservationService.getListByUser(reservationFilter.getBookingStartDate(), reservationFilter.getBookingEndDate(), user)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }
}
