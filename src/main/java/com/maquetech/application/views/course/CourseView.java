package com.maquetech.application.views.course;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.components.maquetech.course.CourseConsultComponent;
import com.maquetech.application.components.maquetech.course.CourseRegistrationComponent;
import com.maquetech.application.services.course.CourseService;
import com.maquetech.application.views.MainLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@RolesAllowed("ADMIN")
@PageTitle("Cursos")
@Route(value = "course", layout = MainLayout.class)
public class CourseView extends MaqueVerticalLayout {

    public CourseView(@Autowired CourseService courseService) {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Consulta", new CourseConsultComponent(courseService));
        tabSheet.add("Cadastro", new CourseRegistrationComponent(courseService));

        add(tabSheet);
    }
}
