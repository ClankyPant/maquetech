package com.maquetech.application.components.maquetech.course;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.models.user.CourseModel;
import com.maquetech.application.services.course.CourseService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CourseSearchComponent extends VerticalLayout {

    public CourseSearchComponent(CourseService courseService) {
        setSizeFull();

        var grid = new MaqueGrid<CourseModel>();
        grid.setSizeFull();
        grid.setColumnReorderingAllowed(true);
        grid.setItems(courseService.getList());
        grid.addColumn(CourseModel::getId).setKey("id").setHeader("Códigos").setSortable(true);
        grid.addColumn(CourseModel::getName).setKey("name").setHeader("Curso").setSortable(true);

        add(grid);
    }
}
