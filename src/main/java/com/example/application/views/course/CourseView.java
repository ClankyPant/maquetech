package com.example.application.views.course;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.maquetech.course.CourseRegistrationComponent;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Cadastro de cursos")
@Route(value = "course-registration", layout = MainLayout.class)
public class CourseView extends MaqueVerticalLayout {

    public CourseView() {

        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Cadastro", new CourseRegistrationComponent());
        tabSheet.add("Consulta", new Div(new Label("Consulta")));

        add(tabSheet);
    }
}
