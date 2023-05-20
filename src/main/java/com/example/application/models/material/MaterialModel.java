package com.example.application.models.material;

import com.example.application.enums.material.MaterialTypeEnum;
import com.example.application.enums.material.MaterialUnitEnum;
import com.example.application.helpers.NotificationHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MaterialModel {

    private Long id;

    private String name;

    private String location;

    private Double stockQty;

    private Double stockSafeQty;

    private Double reservationQuantity = 0D;

    private MaterialTypeEnum type;

    private MaterialUnitEnum unit;

    @Builder.Default
    private boolean onReservation = false;

    private final Button addMaterialButton = new Button(VaadinIcon.CHECK.create());

    private final Button removeMaterialButton = new Button(VaadinIcon.TRASH.create());

    private final NumberField reservationQuantityField = new NumberField();

    public String getTypeDescription() {
        return this.type.getDescription();
    }

    public String getUnitDescription() {
        return this.unit.getDescription();
    }

    public Component getReservationQuantityComponent() {
        reservationQuantityField.setMin(1);
        reservationQuantityField.setStep(1);
        reservationQuantityField.setWidth("35%");
        reservationQuantityField.setAllowedCharPattern("[0-9]");
        reservationQuantityField.addValueChangeListener(event -> {
            var reservationQuantity = event.getValue();
            this.setReservationQuantity(reservationQuantity);
        });

        var vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        vlContent.setAlignItems(FlexComponent.Alignment.CENTER);
        vlContent.add(reservationQuantityField);
        return vlContent;
    }

    public Component getActionButtons() {
        removeMaterialButton.setEnabled(false);

        addMaterialButton.addClickListener(event -> addMaterial());
        removeMaterialButton.addClickListener(event -> removeMaterial());

        var hlContent = new HorizontalLayout();
        hlContent.setSpacing(true);
        hlContent.setAlignItems(FlexComponent.Alignment.CENTER);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlContent.add(addMaterialButton, removeMaterialButton);

        return hlContent;
    }

    public void addMaterial() {
        var reservationQuantity = this.getReservationQuantity();

        if (reservationQuantity == null || reservationQuantity < 1) {
            NotificationHelper.error("Configure uma quantidade válida!");
            return;
        }

        this.setOnReservation(Boolean.TRUE);
        addMaterialButton.setEnabled(Boolean.FALSE);
        removeMaterialButton.setEnabled(Boolean.TRUE);
        this.reservationQuantityField.setReadOnly(Boolean.TRUE);
    }

    public void removeMaterial() {
        this.setOnReservation(Boolean.FALSE);
        addMaterialButton.setEnabled(Boolean.TRUE);
        removeMaterialButton.setEnabled(Boolean.FALSE);
        this.reservationQuantityField.setReadOnly(Boolean.FALSE);
    }
}
