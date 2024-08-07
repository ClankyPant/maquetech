package com.maquetech.application.models.material;

import com.maquetech.application.entities.material.MaterialEntity;
import com.maquetech.application.enums.material.MaterialTypeEnum;
import com.maquetech.application.enums.material.MaterialUnitEnum;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MaterialModel {

    private Long id;

    private String name;

    private String location;

    private Double stockQty;

    private Double t;

    private Double stockSafeQty;

    private MaterialTypeEnum type;

    private MaterialUnitEnum unit;

    private MaterialEntity entidade;

    private Button addMaterialButton;

    private Button removeMaterialButton;

    @Builder.Default
    private boolean onReservation = false;

    @Builder.Default
    private Double reservationQuantity = 0D;

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
        reservationQuantityField.setWidth("50px");
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

    public Component getReservationActionButtons() {
        this.addMaterialButton = new Button(VaadinIcon.CHECK.create());
        this.removeMaterialButton = new Button(VaadinIcon.TRASH.create());

        this.addMaterialButton.setEnabled(!this.onReservation);
        this.removeMaterialButton.setEnabled(this.onReservation);

        addMaterialButton.addClickListener(event -> {
            try {
                addMaterial();
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });
        removeMaterialButton.addClickListener(event -> {
            try {
                removeMaterial();
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });

        var hlContent = new HorizontalLayout();
        hlContent.setSpacing(true);
        hlContent.setAlignItems(FlexComponent.Alignment.CENTER);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hlContent.add(addMaterialButton, removeMaterialButton);

        return hlContent;
    }

    public void validateStockReservation() {
        var reservationQuantity = ConvertHelper.getDouble(this.getReservationQuantity(), 0D);
        var stockQuantity = ConvertHelper.getDouble(this.getStockQty(), 0D);

        if (reservationQuantity <= 0) {
            throw new IllegalArgumentException("Configure uma quantidade maior que zero!");
        }

        if (stockQuantity < reservationQuantity) {
            throw new IllegalArgumentException("Quantidade inválida. Estoque atual: " + stockQuantity);
        }
    }

    public void addMaterial() {
        validateStockReservation();

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
