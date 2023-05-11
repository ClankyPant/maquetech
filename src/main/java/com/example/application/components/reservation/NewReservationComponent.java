package com.example.application.components.reservation;

import com.example.application.entities.reservation.ReservationEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.*;
import java.util.Date;

public class NewReservationComponent extends Dialog {

    private final TabSheet tabSheet = new TabSheet();

    private final Binder<ReservationEntity> binder = new Binder<>();
    private final DateTimePicker startDateTimePicket = new DateTimePicker("Data inicio");
    private final DateTimePicker endDateTimePicket = new DateTimePicker("Data fim");

    public NewReservationComponent() {
        initialize();

        tabSheet.setSizeFull();
        tabSheet.add("Etapa 1", getFirstStep());
        tabSheet.add("Etapa 2", new Label()).setEnabled(false);

        add(tabSheet);
    }

    public void initialize() {
        setWidth("45%");
        setHeight("40%");

        var btnClose = new Button(new Icon(VaadinIcon.CLOSE));
        btnClose.addClickListener(event -> close());

        setHeaderTitle("Nova reserva");
        getHeader().add(btnClose);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

    public LocalDateTime getInitialDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());

    }

    public LocalDateTime getInitialDateTimePlusOneHour() {
        return getInitialDateTime().plusHours(1);
    }

    public Component getFirstStep() {
        var vlContent = new VerticalLayout();
        vlContent.setSpacing(true);
        vlContent.setAlignItems(FlexComponent.Alignment.CENTER);
        vlContent.add(startDateTimePicket, endDateTimePicket);

        var initialDateTime = getInitialDateTime();
        startDateTimePicket.setValue(initialDateTime);
        startDateTimePicket.setMin(initialDateTime);

        var finalDate = getInitialDateTimePlusOneHour();
        endDateTimePicket.setValue(finalDate);
        endDateTimePicket.setMin(initialDateTime);

        binder.forField(startDateTimePicket)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationEntity::getBookingStartDate, ReservationEntity::setBookingStartDate);

        binder.forField(endDateTimePicket)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationEntity::getBookingEndDate, ReservationEntity::setBookingEndDate);

        return vlContent;
    }

    public static class ConvertLocalDateTimeToDate implements Converter<LocalDateTime, Date> {

        @Override
        public Result<Date> convertToModel(LocalDateTime localDateTime, ValueContext valueContext) {
            Result<Date> result = null;

            try {
                if (localDateTime == null) {
                    throw new IllegalArgumentException("Informe data e hora antes de prosseguir");
                }

                result = Result.ok(Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant()));
            } catch (Exception ex) {
                ex.printStackTrace();
                result = Result.error(ex.getMessage());
            }

            return result;
        }

        @Override
        public LocalDateTime convertToPresentation(Date date, ValueContext valueContext) {
            return date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        }
    }
}
