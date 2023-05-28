package com.maquetech.application.components.reservation.user;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.components.reservation.ReservationRegistrationComponent;
import com.maquetech.application.converters.ConvertLocalDateTimeToDate;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.LabelHelper;
import com.maquetech.application.helpers.LocalDateTimeHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.reservation.ReservationMaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.models.reservation.filter.ReservationFilterModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import javassist.NotFoundException;

import java.time.Duration;
import java.util.Locale;

public class ReservationUserComponent extends VerticalLayout {

    private final UserEntity loggedUser;
    private final Dialog messageDialog = new Dialog();
    private final ReservationService reservationService;
    private final Dialog seeMaterialDialog = new Dialog();
    private final MaqueGrid<ReservationModel> grid = new MaqueGrid<>();
    private final ReservationRegistrationComponent reservationRegistrationComponent;
    private final Binder<ReservationFilterModel> binder = new Binder<>();

    public ReservationUserComponent(MaterialService materialService, ReservationService reservationService) throws NotFoundException {
        this.reservationRegistrationComponent = new ReservationRegistrationComponent(materialService, reservationService);
        this.reservationService = reservationService;
        this.loggedUser = UserHelper.getLoggedUser();

        init();
    }

    private void init() {
        createGrid();
        setSizeFull();
        setSpacing(true);
        add(getHeader(), grid, reservationRegistrationComponent, messageDialog, seeMaterialDialog);
        loadGridData();
    }

    private Component getActionButton(ReservationModel reservationModel) {
        var btnVerify = new Button(VaadinIcon.BOOK.create());
        btnVerify.addClickListener(event -> openMessageDialog(reservationModel));
        btnVerify.setTooltipText("Ver mensagens");

        var btnCancel = new Button(VaadinIcon.TRASH.create());
        btnCancel.setTooltipText("Cancelar reserva");
        btnCancel.setEnabled(reservationModel.isntCanceled() && reservationModel.isntInProgress() && reservationModel.isntFinished());
        btnCancel.addClickListener(event -> {
            try {
                grid.getDataProvider().refreshItem(reservationService.cancel(reservationModel.getId(), reservationModel));
                NotificationHelper.success("Reserva " + reservationModel.getId() + " cancelada com sucesso!");
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });

        var btnSee = new Button(VaadinIcon.SEARCH.create());
        btnSee.setTooltipText("Ver materiais");
        btnSee.addClickListener(event -> openSeeMaterial(reservationModel));

        var hlContent = new HorizontalLayout();
        hlContent.add(btnVerify, btnCancel, btnSee);
        hlContent.setJustifyContentMode(JustifyContentMode.CENTER);

        return hlContent;
    }

    private Component getHeader() {
        var startDateTimePicker = new DateTimePicker("Data inicio");
        startDateTimePicker.setLocale(new Locale("pt", "BR"));
        startDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        startDateTimePicker.setValue(LocalDateTimeHelper.now());
        startDateTimePicker.setStep(Duration.ofMinutes(15));
        binder.forField(startDateTimePicker)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingStartDate, ReservationFilterModel::setBookingStartDate);

        var endDateTimePicker = new DateTimePicker("Data fim");
        endDateTimePicker.setLocale(new Locale("pt", "BR"));
        endDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        endDateTimePicker.setValue(LocalDateTimeHelper.getNowPlusTwoWeek());
        endDateTimePicker.setStep(Duration.ofMinutes(15));
        binder.forField(endDateTimePicker)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingEndDate, ReservationFilterModel::setBookingEndDate);

        var btnConsult = new Button("Consultar", VaadinIcon.SEARCH.create(), event -> loadGridData());
        btnConsult.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        var formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0px", 1),
                new FormLayout.ResponsiveStep("1000px", 4)
        );
        formLayout.add(
                startDateTimePicker,
                endDateTimePicker,
                btnConsult,
                new Button("Nova reserva", event -> reservationRegistrationComponent.open())
        );

        return formLayout;
    }

    private void createGrid() {
        grid.addColumn(ReservationModel::getId).setKey("id").setHeader("Código").setTextAlign(ColumnTextAlign.CENTER);
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
                    this.reservationService.getListByUser(reservationFilter.getBookingStartDate(), reservationFilter.getBookingEndDate(), loggedUser.getId(), null)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }

    public void openMessageDialog(ReservationModel reservationModel) {
        messageDialog.removeAll();
        messageDialog.getFooter().removeAll();
        messageDialog.setWidth("95%");
        messageDialog.setHeight("95%");

        var message = new TextArea("Messagem");
        message.setSizeFull();
        message.setReadOnly(true);
        message.setValue(reservationModel.getMessage());

        messageDialog.add(message);
        messageDialog.getFooter().add(new Button("Fechar", event -> messageDialog.close()));
        messageDialog.open();
    }

    private void openSeeMaterial(ReservationModel reservationModel) {
        seeMaterialDialog.removeAll();
        seeMaterialDialog.getFooter().removeAll();
        seeMaterialDialog.setWidth("50%");
        seeMaterialDialog.setHeight("50%");

        var seeMaterialGrid = new MaqueGrid<ReservationMaterialModel>();
        seeMaterialGrid.addColumn(ReservationMaterialModel::getMaterialId).setKey("material_id").setHeader("Cód. material").setTextAlign(ColumnTextAlign.CENTER);
        seeMaterialGrid.addColumn(ReservationMaterialModel::getMaterialName).setKey("material_name").setHeader("Material").setTextAlign(ColumnTextAlign.CENTER);
        seeMaterialGrid.addColumn(ReservationMaterialModel::getQuantity).setKey("material_qty").setHeader("Quantidade").setTextAlign(ColumnTextAlign.CENTER);
        seeMaterialGrid.setItems(reservationModel.getReservationMaterialList());
        seeMaterialGrid.setSizeFull();
        seeMaterialDialog.add(seeMaterialGrid);

        seeMaterialDialog.getFooter().add(
                new Button("Fechar", event -> seeMaterialDialog.close())
        );
        seeMaterialDialog.open();
    }
}
