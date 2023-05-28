package com.maquetech.application.views.user;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.components.professor.ProfessorCodeGeneratorComponent;
import com.maquetech.application.components.user.search.UserSearchComponent;
import com.maquetech.application.services.course.CourseService;
import com.maquetech.application.services.user.UserService;
import com.maquetech.application.services.user.professor.ProfessorCodeService;
import com.maquetech.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import javassist.NotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@RolesAllowed("ADMIN")
@PageTitle("Usuários")
@Route(value = "usuario", layout = MainLayout.class)
public class UserView extends MaqueVerticalLayout {

    public UserView(ProfessorCodeService professorCodeService, UserService userService, InMemoryUserDetailsManager inMemoryUserDetailsManager, CourseService courseService) throws NotFoundException {
        setAlignItems(FlexComponent.Alignment.START);
        setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Usuários", new UserSearchComponent(userService, courseService, professorCodeService, inMemoryUserDetailsManager));
        tabSheet.add("Gerador de código", new ProfessorCodeGeneratorComponent(professorCodeService));

        add(tabSheet);
    }
}
