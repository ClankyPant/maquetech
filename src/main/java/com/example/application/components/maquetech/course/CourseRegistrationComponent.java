package com.example.application.components.maquetech.course;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.entities.course.CourseEntity;
import com.example.application.helpers.NotificationHelper;
import com.example.application.views.user.UserLoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class CourseRegistrationComponent extends MaqueVerticalLayout {

    public CourseRegistrationComponent() {
        Binder<CourseEntity> binder = new Binder<>(CourseEntity.class);

        TextField nameField = new TextField("Nome do curso");
        nameField.setAllowedCharPattern("[A-Za-z0-9]");
        nameField.setRequired(true);
        binder.forField(nameField)
                .withValidator(name -> name.length() > 3, "Nome do curso deve conter mais que 3 caracteres!")
                .bind(CourseEntity::getName, CourseEntity::setName);

        Button btnRegister = new Button("Cadastrar");
        btnRegister.addClickListener((event) -> {
            try {
                CourseEntity courseEntity = new CourseEntity();
                binder.writeBean(courseEntity);

//                this.userService.save(courseEntity);
                this.getUI().ifPresent(ui -> ui.navigate(UserLoginView.class));
            } catch (ValidationException ex) {
                NotificationHelper.error("Alguns campos não foram preenchidos corretamente!");
            } catch (Exception e) {
                NotificationHelper.error(e.getMessage());
            }
        });

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSizeUndefined();
        vlContent.add(new FormLayout(nameField), btnRegister);

        add(vlContent);
    }
}
