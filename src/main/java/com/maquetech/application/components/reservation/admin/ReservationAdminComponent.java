package com.maquetech.application.components.reservation.admin;

import com.maquetech.application.components.reservation.NewReservationComponent;
import com.maquetech.application.converters.ConvertLocalDateTimeToDate;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.reservation.SituationEnum;
import com.maquetech.application.helpers.LabelHelper;
import com.maquetech.application.helpers.LocalDateTimeHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.UserHelper;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.models.reservation.filter.ReservationFilterModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import javassist.NotFoundException;

import java.time.Duration;
import java.util.Locale;

public class ReservationAdminComponent extends VerticalLayout {

    private final UserEntity loggedUser;
    private final Dialog messageDialog = new Dialog();
    private final ReservationService reservationService;
    private final Grid<ReservationModel> grid = new Grid<>();
    private final Binder<ReservationFilterModel> binder = new Binder<>();

    public ReservationAdminComponent(ReservationService reservationService) throws NotFoundException {
        this.reservationService = reservationService;
        this.loggedUser = UserHelper.getLoggedUser();

        init();
    }

    private Component getActionButton(ReservationModel reservationModel) {
        var hlContent = new HorizontalLayout();
        hlContent.setJustifyContentMode(JustifyContentMode.CENTER);

        return hlContent;
    }

    private void init() {
        createGrid();
        setSizeFull();
        setSpacing(true);
        add(getHeader(), grid, messageDialog);
        loadGridData();
    }

    private Component getHeader() {
        var startDateTimePicker = new DateTimePicker();
        startDateTimePicker.setLocale(new Locale("pt", "BR"));
        startDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        startDateTimePicker.setValue(LocalDateTimeHelper.now());
        startDateTimePicker.setStep(Duration.ofMinutes(15));
        binder.forField(startDateTimePicker)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingStartDate, ReservationFilterModel::setBookingStartDate);

        var endDateTimePicket = new DateTimePicker();
        startDateTimePicker.setLocale(new Locale("pt", "BR"));
        endDateTimePicket.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        endDateTimePicket.setValue(LocalDateTimeHelper.getNowPlus1HourAnd15Minutes());
        endDateTimePicket.setStep(Duration.ofMinutes(15));
        binder.forField(endDateTimePicket)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingEndDate, ReservationFilterModel::setBookingEndDate);

        var btnConsult = new Button("Consultar");
        btnConsult.addClickListener(event -> loadGridData());

        var formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0px", 1),
                new FormLayout.ResponsiveStep("1000px", 3)
        );
        formLayout.add(
                startDateTimePicker,
                endDateTimePicket,
                btnConsult
        );

        return formLayout;
    }

    private void createGrid() {
        grid.addColumn(ReservationModel::getId).setKey("id").setHeader("Código").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(ReservationModel::getUserName).setKey("user_name").setHeader("Usuário").setTextAlign(ColumnTextAlign.CENTER);
        var startDateColumn = grid.addColumn(ReservationModel::getStartDateDisplay).setKey("start_date").setHeader("Data").setTextAlign(ColumnTextAlign.CENTER);
        var startHourColumn = grid.addColumn(ReservationModel::getStartHourDisplay).setKey("start_hour").setHeader("Hora").setTextAlign(ColumnTextAlign.CENTER);
        var endDateColumn = grid.addColumn(ReservationModel::getEndDateDisplay).setKey("end_date").setHeader("Data").setTextAlign(ColumnTextAlign.CENTER);
        var endHourColumn = grid.addColumn(ReservationModel::getEndHourDisplay).setKey("end_hour").setHeader("Hora").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(ReservationModel::getSituationDisplay).setKey("situation").setHeader("Situação").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(this::getActionButton).setKey("action_button").setHeader("Ações").setTextAlign(ColumnTextAlign.CENTER);

        var headerRow = grid.prependHeaderRow();
        headerRow.join(startDateColumn, startHourColumn).setComponent(LabelHelper.getCenteredLabel("Inicío"));
        headerRow.join(endDateColumn, endHourColumn).setComponent(LabelHelper.getCenteredLabel("Fim"));
    }

    private void loadGridData() {
        try {
            var reservationFilter = new ReservationFilterModel();
            binder.writeBean(reservationFilter);

            this.grid.setItems(
                    this.reservationService.getListByUser(reservationFilter.getBookingStartDate(), reservationFilter.getBookingEndDate(), null)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }
}
