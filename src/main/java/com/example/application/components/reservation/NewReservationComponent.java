package com.example.application.components.reservation;

import com.example.application.entities.reservation.ReservationEntity;
import com.example.application.helpers.NotificationHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

    private final Button btnNext = new Button("Próximo");
    private final Button btnPrevius = new Button("Anterior");
    private final Binder<ReservationEntity> binder = new Binder<>();
    private final DateTimePicker endDateTimePicket = new DateTimePicker("Data fim");
    private final DateTimePicker startDateTimePicket = new DateTimePicker("Data inicio");

    public NewReservationComponent() {
        initialize();

        tabSheet.setSizeFull();
        tabSheet.add("Etapa 1", getFirstStep());
        tabSheet.add("Etapa 2", new Label()).setEnabled(false);

        add(tabSheet);
        getFooter().add(createFooter());
    }

    public Component createFooter() {
        btnNext.setIcon(VaadinIcon.ARROW_RIGHT.create());
        btnNext.setIconAfterText(true);
        btnNext.addClickListener(event -> {
            try {
                var startDate = this.startDateTimePicket.getValue();
                var endDate = this.endDateTimePicket.getValue();

                if (startDate.isAfter(endDate)) throw new IllegalArgumentException("Data inicio deve ser menor que data fim!");

                selectTabByIndex(1);
                btnPrevius.setEnabled(true);
                btnNext.setEnabled(false);

                setWidth("75%");
                setHeight("75%");
            } catch (Exception ex) {
                NotificationHelper.error(ex.getMessage());
            }
        });

        btnPrevius.setIcon(VaadinIcon.ARROW_LEFT.create());
        btnPrevius.setEnabled(false);
        btnPrevius.setIconAfterText(false);
        btnPrevius.addClickListener(event -> {
            selectTabByIndex(0);
            btnPrevius.setEnabled(false);
            btnNext.setEnabled(true);

            setWidth("45%");
            setHeight("40%");
        });

        var hlContent = new HorizontalLayout();
        hlContent.setSizeFull();
        hlContent.add(btnPrevius, btnNext);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        return hlContent;
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
        return LocalDateTime.now(ZoneId.systemDefault()).plusHours(1);
    }

    public LocalDateTime getInitialDateTimePlusOneHour() {
        return getInitialDateTime().plusHours(1);
    }

    public Component getFirstStep() {
        var initialDateTime = getInitialDateTime();
        startDateTimePicket.setMin(initialDateTime);
        startDateTimePicket.setValue(initialDateTime);

        var finalDate = getInitialDateTimePlusOneHour();
        endDateTimePicket.setValue(finalDate);
        endDateTimePicket.setMin(initialDateTime);

        binder.forField(startDateTimePicket)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationEntity::getBookingStartDate, ReservationEntity::setBookingStartDate);

        binder.forField(endDateTimePicket)
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationEntity::getBookingEndDate, ReservationEntity::setBookingEndDate);

        var vlContent = new VerticalLayout();
        vlContent.setSpacing(true);
        vlContent.setAlignItems(FlexComponent.Alignment.CENTER);
        vlContent.add(startDateTimePicket, endDateTimePicket);
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

    public void selectTabByIndex(int index) {
        var selectedTab = tabSheet.getSelectedTab();
        selectedTab.setEnabled(false);

        var newTab = tabSheet.getTabAt(index);
        newTab.setEnabled(true);
        tabSheet.setSelectedTab(newTab);
    }
}
