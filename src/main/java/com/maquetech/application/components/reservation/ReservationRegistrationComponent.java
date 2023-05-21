package com.maquetech.application.components.reservation;

import com.maquetech.application.components.material.consult.MaterialSearchComponent;
import com.maquetech.application.converters.ConvertLocalDateTimeToDate;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.LocalDateTimeHelper;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.UserHelper;
import com.maquetech.application.models.reservation.ReservationModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.reservation.ReservationService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.data.binder.ValidationException;
import javassist.NotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

public class ReservationRegistrationComponent extends Dialog {

    private final TabSheet tabSheet = new TabSheet();

    private final UserEntity loggedUser;
    private final ReservationService reservationService;
    private final Button btnFinish = new Button("Finalizar");
    private final Button btnNext = new Button("Próximo");
    private final Button btnPrevius = new Button("Anterior");
    private final Binder<ReservationModel> binder = new Binder<>();
    private final MaterialSearchComponent materialSearchComponent;
    private final DateTimePicker endDateTimePicker = new DateTimePicker("Data fim");
    private final DateTimePicker startDateTimePicker = new DateTimePicker("Data inicio");

    public ReservationRegistrationComponent(MaterialService materialService, ReservationService reservationService) throws NotFoundException {
        this.materialSearchComponent = new MaterialSearchComponent(materialService, false);
        this.reservationService = reservationService;
        this.loggedUser = UserHelper.getLoggedUser();

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

        btnFinish.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        btnFinish.setEnabled(false);
        btnFinish.addClickListener(event -> {
            try {
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });

        btnPrevius.setIcon(VaadinIcon.ARROW_LEFT.create());
        btnPrevius.setEnabled(false);
        btnPrevius.setIconAfterText(false);
        btnPrevius.addClickListener(event -> selectTabByIndex(0));

        var hlRight = new HorizontalLayout();
        hlRight.setSpacing(true);
        hlRight.setSizeUndefined();
        hlRight.add(btnFinish, btnNext);

        var hlContent = new HorizontalLayout();
        hlContent.setSizeFull();
        hlContent.add(btnPrevius, hlRight);
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        getFooter().add(hlContent);
    }

    public void init() {
        setSizeFull();

        var btnClose = new Button(new Icon(VaadinIcon.CLOSE));
        btnClose.addClickListener(event -> close());

        setHeaderTitle("Nova reserva");
        getHeader().add(btnClose);

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        tabSheet.setSizeFull();
        tabSheet.add("Etapa 1", getFirstStep());
        tabSheet.add("Etapa 2", materialSearchComponent).setEnabled(false);

        add(tabSheet);
    }

    private void finish() throws ValidationException {
        var reservationModel = getReservationModel();
        var materialModelList = materialSearchComponent.getOnReservationListAndValidate();
        this.reservationService.create(materialModelList, reservationModel, this.loggedUser);
        NotificationHelper.success("Reserva feita com sucesso!");
        this.close();
    }

    private ReservationModel getReservationModel() throws ValidationException {
        var reservationModel = new ReservationModel();
        binder.writeBean(reservationModel);
        return reservationModel;
    }

    @Override
    public void open() {
        startDateTimePicker.setValue(LocalDateTimeHelper.getNowPlus15Minutes());
        endDateTimePicker.setValue(LocalDateTimeHelper.getNowPlus1HourAnd15Minutes());

        selectTabByIndex(0);

        super.open();
    }

    public Component getFirstStep() {
        var now = LocalDateTime.now();

        startDateTimePicker.setLocale(new Locale("pt", "BR"));
        startDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        startDateTimePicker.setValue(LocalDateTimeHelper.getNowPlus15Minutes());
        startDateTimePicker.setStep(Duration.ofMinutes(15));
        startDateTimePicker.setMin(now);

        endDateTimePicker.setLocale(new Locale("pt", "BR"));
        endDateTimePicker.addThemeVariants(DateTimePickerVariant.LUMO_SMALL);
        endDateTimePicker.setValue(LocalDateTimeHelper.getNowPlus1HourAnd15Minutes());
        endDateTimePicker.setStep(Duration.ofMinutes(15));
        endDateTimePicker.setMin(now);

        binder.forField(startDateTimePicker)
                .withValidator(Objects::nonNull, "Informe uma data")
                .withValidator(dateTime -> dateTime.isAfter(LocalDateTime.now()), "Data precisa válido!")
                .withValidator(dateTime -> dateTime.isBefore(endDateTimePicker.getValue()), "Data inicio deve ser menor que data fim!")
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationModel::getBookingStartDate, ReservationModel::setBookingStartDate);

        binder.forField(endDateTimePicker)
                .withValidator(Objects::nonNull, "Informe uma data")
                .withValidator(dateTime -> dateTime.isAfter(LocalDateTime.now()), "Data precisa válido!")
                .withConverter(new ConvertLocalDateTimeToDate())
                .bind(ReservationModel::getBookingEndDate, ReservationModel::setBookingEndDate);

        var formLayout = new FormLayout();
        formLayout.add(startDateTimePicker, endDateTimePicker);
        return formLayout;
    }

    public void selectTabByIndex(int index) {
        var isFirstStep = index == 0;
        if (!isFirstStep) resetConsultConfiguration();

        btnPrevius.setEnabled(!isFirstStep);
        btnFinish.setEnabled(!isFirstStep);
        btnNext.setEnabled(isFirstStep);

        var selectedTab = tabSheet.getSelectedTab();
        selectedTab.setEnabled(false);

        var newTab = tabSheet.getTabAt(index);
        newTab.setEnabled(true);
        tabSheet.setSelectedTab(newTab);
    }

    private void resetConsultConfiguration() {
        try {
            var reservationModel = getReservationModel();
            this.materialSearchComponent.resetReservationConsult(reservationModel.getBookingStartDate(), reservationModel.getBookingEndDate());
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }
}
