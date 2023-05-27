package com.maquetech.application.views;


import com.maquetech.application.components.appnav.AppNav;
import com.maquetech.application.components.appnav.AppNavItem;
import com.maquetech.application.components.user.UserNavComponent;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.services.user.UserService;
import com.maquetech.application.views.course.CourseView;
import com.maquetech.application.views.material.MaterialView;
import com.maquetech.application.views.reservation.ReservationView;
import com.maquetech.application.views.user.UserView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import javassist.NotFoundException;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
@PermitAll
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private final UserEntity userLogged;
    private final UserService userService;

    public MainLayout(UserService userService) throws NotFoundException {
        this.userService = userService;

        this.userLogged = UserHelper.getLoggedUser();
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() throws NotFoundException {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        HorizontalLayout hlNavBarInfo = new HorizontalLayout();
        hlNavBarInfo.setMargin(true);
        hlNavBarInfo.setSizeFull();

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        hlNavBarInfo.add(viewTitle, new UserNavComponent(userService));
        hlNavBarInfo.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        hlNavBarInfo.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(true, toggle, hlNavBarInfo);
    }

    private void addDrawerContent() {
        H1 appName = new H1("MaqueTech");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        AppNav nav = new AppNav();

        nav.addItem(new AppNavItem("Reservas", ReservationView.class, LineAwesomeIcon.CALENDAR.create(), true));
        nav.addItem(new AppNavItem("Materiais", MaterialView.class, LineAwesomeIcon.TOOLBOX_SOLID.create(), this.userLogged.isAdmin()));
        nav.addItem(new AppNavItem("Usuários", UserView.class, LineAwesomeIcon.USER.create(), this.userLogged.isAdmin()));
        nav.addItem(new AppNavItem("Cursos", CourseView.class, LineAwesomeIcon.BOOK_SOLID.create(), this.userLogged.isAdmin()));

        return nav;
    }

    private Footer createFooter() {
        return new Footer();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
