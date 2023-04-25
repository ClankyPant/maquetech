package com.example.application.views.course;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.maquetech.course.CourseConsultComponent;
import com.example.application.components.maquetech.course.CourseRegistrationComponent;
import com.example.application.services.course.CourseService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed("ADMIN")
@PageTitle("Cadastro de cursos")
@Route(value = "course-registration", layout = MainLayout.class)
public class CourseView extends MaqueVerticalLayout {

    public CourseView(@Autowired CourseService courseService) {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Cadastro", new CourseRegistrationComponent(courseService));
        tabSheet.add("Consulta", new CourseConsultComponent(courseService));

        add(tabSheet);
    }
}