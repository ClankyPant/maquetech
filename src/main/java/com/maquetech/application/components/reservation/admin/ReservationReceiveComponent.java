package com.maquetech.application.components.reservation.admin;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.models.reservation.ReservationMaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.services.reservation.ReservationReceiveService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;

public class ReservationReceiveComponent extends Dialog {

    private Runnable receiveFunc;
    private ReservationModel reservationModel;
    private final ReservationReceiveService reservationReceiveService;
    private final MaqueGrid<ReservationMaterialModel> table = new MaqueGrid<>();

    public ReservationReceiveComponent(ReservationReceiveService reservationReceiveService) {
        this.reservationReceiveService = reservationReceiveService;
        setHeaderTitle("Justificativa de reserva");

        init();
        initFooter();
    }

    private Component getDamageMaterial(ReservationMaterialModel reservationMaterialModel) {
        var damageField = new NumberField();
        damageField.setStep(1);
        damageField.setWidth("50px");
        damageField.addValueChangeListener(event -> {
            var requestedQuantity = reservationMaterialModel.getQuantity();
            var inputedQuantity = ConvertHelper.getDouble(ConvertHelper.getDouble(event.getValue(), 0D).intValue());

            var newValue = requestedQuantity < inputedQuantity ? requestedQuantity : inputedQuantity;
            event.getSource().setValue(newValue);
            reservationMaterialModel.setDamageQuantity(newValue);
        });

        return damageField;
    }

    private void init() {
        table.addColumn(ReservationMaterialModel::getMaterialId).setKey("material_id").setHeader("Cód. material").setTextAlign(ColumnTextAlign.CENTER);
        table.addColumn(ReservationMaterialModel::getMaterialName).setKey("material_name").setHeader("Material").setTextAlign(ColumnTextAlign.CENTER);
        table.addColumn(ReservationMaterialModel::getQuantity).setKey("material_qty").setHeader("Quantidade").setTextAlign(ColumnTextAlign.CENTER);
        table.addComponentColumn(this::getDamageMaterial).setKey("damage_material").setHeader("Qtd. danificada").setTextAlign(ColumnTextAlign.CENTER);
        table.setSizeFull();

        setSizeFull();
        add(table);
    }

    private void initFooter() {
        var btnCancel = new Button("Cancelar", event -> this.close());
        btnCancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        var btnFinish = new Button("Finalizar");
        btnFinish.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnFinish.addClickListener(event -> {
            reservationReceiveService.reduceReservationQuantity(this.reservationModel);
            receiveFunc.run();
            this.close();
        });

        var hlContent = new HorizontalLayout();
        hlContent.setSizeFull();
        hlContent.add(btnCancel, btnFinish);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        getFooter().add(hlContent);
    }

    public void open(ReservationModel reservationModel, Runnable receiveFunc) {
        this.receiveFunc = receiveFunc;
        this.reservationModel = reservationModel;
        table.setItems(this.reservationModel.getReservationMaterialList());
        super.open();
    }
}
