package com.maquetech.application.components.reservation;

import com.maquetech.application.components.material.consult.MaterialConsultComponent;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePickerVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import javassist.NotFoundException;

import java.time.*;
import java.util.Date;
import java.util.Objects;

public class NewReservationComponent extends Dialog {

    private final TabSheet tabSheet = new TabSheet();

    private final Button btnNext = new Button("Próximo");
    private final Button btnPrevius = new Button("Anterior");
    private final Binder<ReservationModel> binder = new Binder<>();
    private final MaterialConsultComponent materialConsultComponent;
    private final DateTimePicker endDateTimePicket = new DateTimePicker("Data fim");
    private final DateTimePicker startDateTimePicket = new DateTimePicker("Data inicio");

    public NewReservationComponent(MaterialService materialService, UserService userService) throws NotFoundException {
        this.materialConsultComponent = new MaterialConsultComponent(materialService, userService, false);

        init();
        initFooter();
    }

    public void initFooter() {
        btnNext.setIcon(VaadinIcon.ARROW_RIGHT.create());
        btnNext.setIconAfterText(true);
        btnNext.addClickListener(event -> {
            if (binder.isValid()) {
                selectTabByIndex(1);
            }
        });

        btnPrevius.setIcon(VaadinIcon.ARROW_LEFT.create());
        btnPrevius.setEnabled(false);
        btnPrevius.setIconAfterText(false);
        btnPrevius.addClickListener(event -> {
            selectTabByIndex(0);
        });

        var hlContent = new HorizontalLayout();
        hlContent.setSizeFull();
        hlContent.add(btnPrevius, btnNext);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        getFooter().add(hlContent);
    }

    public void init() {
        setWidth("45%");
        setHeight("40%");

        var btnClose = new Button(new Icon(VaadinIcon.CLOSE));
        btnClose.addClickListener(event -> close());

        setHeaderTitle("Nova reserva");
        getHeader().add(btnClose);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        tabSheet.setSizeFull();
        tabSheet.add("Etapa 1", getFirstStep());
        tabSheet.add("Etapa 2", materialConsultComponent).setEnabled(false);

        add(tabSheet);
    }

    public LocalDateTime getInitialDateTime() {
        var now = LocalDateTime.now();

        while ((now.getMinute() % 15) != 0) {
            now = now.plusMinutes(1);
        }

        return now;
    }

    @Override
    public void open() {
        var startDateTime = getInitialDateTime();
        var endDateTime = getInitialDateTimePlusOneHour();

        startDateTimePicket.setValue(startDateTime);
        endDateTimePicket.setValue(endDateTime);

        selectTabByIndex(0);

        super.open();
    }

    public LocalDateTime getInitialDateTimePlusOneHour() {
        return getInitialDateTime().plusHours(1);
    }

    public Component getFirstStep() {
        var now = LocalDateTime.now();

        var initialDateTime = getInitialDateTime();
        startDateTimePicket.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        startDateTimePicket.setValue(initialDateTime);
        startDateTimePicket.setStep(Duration.ofMinutes(15));
        startDateTimePicket.setMin(now);

        var finalDate = getInitialDateTimePlusOneHour();
        endDateTimePicket.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        endDateTimePicket.setValue(finalDate);
        endDateTimePicket.setStep(Duration.ofMinutes(15));
        endDateTimePicket.setMin(now);

        binder.forField(startDateTimePicket)
                .withValidator(Objects::nonNull, "Informe uma data")
                .withValidator(dateTime -> dateTime.isAfter(LocalDateTime.now()), "Data precisa válido!")
                .withValidator(dateTime -> dateTime.isBefore(endDateTimePicket.getValue()), "Data inicio deve ser menor que data fim!")
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationModel::getBookingStartDate, ReservationModel::setBookingStartDate);

        binder.forField(endDateTimePicket)
                .withValidator(Objects::nonNull, "Informe uma data")
                .withValidator(dateTime -> dateTime.isAfter(LocalDateTime.now()), "Data precisa válido!")
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationModel::getBookingEndDate, ReservationModel::setBookingEndDate);

        var formLayout = new FormLayout();
        formLayout.add(startDateTimePicket, endDateTimePicket);
        return formLayout;
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
        var isFirtStep = index == 0;
        setWidth(isFirtStep ? "65%" : "75%");
        setHeight(isFirtStep ? "450px" : "750px");
        btnPrevius.setEnabled(!isFirtStep);
        btnNext.setEnabled(isFirtStep);

        var selectedTab = tabSheet.getSelectedTab();
        selectedTab.setEnabled(false);

        var newTab = tabSheet.getTabAt(index);
        newTab.setEnabled(true);
        tabSheet.setSelectedTab(newTab);
    }
}
