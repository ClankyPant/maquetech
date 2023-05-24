package com.maquetech.application.components.reservation.admin;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.models.reservation.ReservationMaterialModel;
import com.maquetech.application.models.reservation.ReservationModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;

import java.awt.*;
import java.nio.Buffer;

public class ReservationReceiveComponent extends Dialog {

    private final MaqueGrid<ReservationMaterialModel> table = new MaqueGrid<>();

    public ReservationReceiveComponent() {
        setHeaderTitle("Justificativa de reserva");

        init();
        initFooter();
    }

    private Component getDamageMaterial(ReservationMaterialModel reservationMaterialModel) {
        var damageField = new NumberField();
        damageField.setStep(1);
        damageField.setWidth("50px");
        damageField.addValueChangeListener(event -> {
           event.getSource().setValue(ConvertHelper.getDouble(ConvertHelper.getDouble(event.getValue(), 0D).intValue()));
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

        var hlContent = new HorizontalLayout();
        hlContent.setSizeFull();
        hlContent.add(btnCancel, btnFinish);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        getFooter().add(hlContent);
    }

    public void open(ReservationModel reservationModel) {
        table.setItems(reservationModel.getMaterialList());
        super.open();
    }
}
