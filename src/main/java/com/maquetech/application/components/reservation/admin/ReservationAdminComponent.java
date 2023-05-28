package com.maquetech.application.components.reservation.admin;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.converters.ConvertLocalDateTimeToDate;
import com.maquetech.application.enums.reservation.SituationEnum;
import com.maquetech.application.helpers.LabelHelper;
import com.maquetech.application.helpers.LocalDateTimeHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.models.reservation.ReservationMaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.models.reservation.filter.ReservationFilterModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationReceiveService;
import com.maquetech.application.services.reservation.ReservationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;

import java.time.Duration;
import java.util.Locale;

public class ReservationAdminComponent extends VerticalLayout {

    private final MaterialService materialService;
    private final Dialog messageDialog = new Dialog();
    private final ReservationService reservationService;
    private final Dialog seeMaterialDialog = new Dialog();
    private final MaqueGrid<ReservationModel> grid = new MaqueGrid<>();
    private final Binder<ReservationFilterModel> binder = new Binder<>();
    private final ReservationReceiveComponent reservationReceiveComponent;

    public ReservationAdminComponent(
            ReservationService reservationService,
            MaterialService materialService,
            ReservationReceiveService reservationReceiveService
    ) {
        this.reservationReceiveComponent = new ReservationReceiveComponent(reservationReceiveService);
        this.reservationService = reservationService;
        this.materialService = materialService;

        init();
    }

    private Component getActionButton(ReservationModel reservationModel) {
        var reservationId = reservationModel.getId();

        var btnAprove = new Button(VaadinIcon.CHECK.create());
        btnAprove.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnAprove.setTooltipText("Aprovar reserva");
        btnAprove.setVisible(reservationModel.isPending());
        btnAprove.addClickListener(event -> {
            openReservationMessage(reservationModel, "Aprovar", () -> {
                NotificationHelper.runAndNotify(() -> {
                    grid.getDataProvider().refreshItem(reservationService.approve(reservationId, reservationModel));
                }, "Aprovado com sucesso!");
            });
        });

        var btnDeliver = new Button(VaadinIcon.UPLOAD.create());
        btnDeliver.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnDeliver.setTooltipText("Entregar materiais");
        btnDeliver.setVisible(reservationModel.isApproved());
        btnDeliver.addClickListener(event -> NotificationHelper.runAndNotify(() -> {
            materialService.validateAndRemoveConsumables(reservationId);
            grid.getDataProvider().refreshItem(reservationService.deliver(reservationId, reservationModel));
        }, "Reserva entregue com sucesso!"));

        var btnReceive = new Button(VaadinIcon.DOWNLOAD.create());
        btnReceive.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnReceive.setTooltipText("Receber materiais");
        btnReceive.setVisible(reservationModel.isInProgress());
        btnReceive.addClickListener(event -> {
            reservationReceiveComponent.open(
                    reservationModel,
                    () -> {
                        NotificationHelper.runAndNotify(
                                () -> grid.getDataProvider().refreshItem(reservationService.receive(reservationId, reservationModel)),
                                "Reserva recebida com sucesso!"
                        );
                    });
        });

        var btnReprove = new Button(VaadinIcon.TRASH.create());
        btnReprove.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnReprove.setTooltipText("Cancelar reserva");
        btnReprove.setVisible(!reservationModel.isInProgress() && !reservationModel.isCanceled() && !reservationModel.isFinished());
        btnReprove.addClickListener(event -> {
            openReservationMessage(reservationModel, "Cancelar reserva", () -> {
                NotificationHelper.runAndNotify(() -> {
                    grid.getDataProvider().refreshItem(reservationService.cancel(reservationId, reservationModel));
                }, "Reserva cancelada com sucesso!");
            });
        });

        var btnSee = new Button(VaadinIcon.SEARCH.create());
        btnSee.setTooltipText("Ver materiais");
        btnSee.addClickListener(event -> openSeeMaterial(reservationModel));

        var hlContent = new HorizontalLayout();
        hlContent.setJustifyContentMode(JustifyContentMode.CENTER);
        hlContent.add(btnAprove, btnDeliver, btnReceive, btnReprove, btnSee);
        return hlContent;
    }

    private void init() {
        createGrid();
        setSizeFull();
        setSpacing(true);
        add(getHeader(), grid, messageDialog, seeMaterialDialog, reservationReceiveComponent);
        loadGridData();
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
        startDateTimePicker.setLocale(new Locale("pt", "BR"));
        endDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        endDateTimePicker.setValue(LocalDateTimeHelper.getNowPlusTwoWeek());
        endDateTimePicker.setStep(Duration.ofMinutes(15));
        binder.forField(endDateTimePicker)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationFilterModel::getBookingEndDate, ReservationFilterModel::setBookingEndDate);

        var situation = new ComboBox<SituationEnum>("Situação");
        situation.setClearButtonVisible(true);
        situation.setItems(SituationEnum.values());
        situation.setItemLabelGenerator(SituationEnum::getDescription);
        binder.forField(situation).bind(ReservationFilterModel::getSituation, ReservationFilterModel::setSituation);

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
                situation,
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
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);

        var headerRow = grid.prependHeaderRow();
        headerRow.join(startDateColumn, startHourColumn).setComponent(LabelHelper.getCenteredLabel("Inicío"));
        headerRow.join(endDateColumn, endHourColumn).setComponent(LabelHelper.getCenteredLabel("Fim"));
    }

    private void loadGridData() {
        try {
            var reservationFilter = new ReservationFilterModel();
            binder.writeBean(reservationFilter);

            this.grid.setItems(
                    this.reservationService.getListByUser(reservationFilter.getBookingStartDate(), reservationFilter.getBookingEndDate(), null, reservationFilter.getSituation())
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }

    private void openReservationMessage(ReservationModel reservationModel, String actionButtonName, Runnable action) {
        messageDialog.removeAll();
        messageDialog.getFooter().removeAll();
        messageDialog.setWidth("50%");
        messageDialog.setHeight("50%");

        var message = new TextArea("Messagem (Opcional):");
        message.setSizeFull();
        messageDialog.add(message);

        messageDialog.getFooter().add(
                new Button("Cancelar", event -> messageDialog.close()),
                new Button(actionButtonName, event -> {
                    reservationModel.setMessage(message.getValue());
                    messageDialog.close();
                    action.run();
                })
        );
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
