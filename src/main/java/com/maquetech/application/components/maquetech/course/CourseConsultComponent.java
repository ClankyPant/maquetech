package com.maquetech.application.components.maquetech.course;

import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.services.course.CourseService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CourseConsultComponent extends VerticalLayout {

    public CourseConsultComponent(CourseService courseService) {
        setSizeFull();

        var grid = new Grid<CourseEntity>();
        grid.setSizeFull();
        grid.setColumnReorderingAllowed(true);
        grid.setItems(courseService.findAll());
        grid.addColumn(CourseEntity::getId).setKey("id").setHeader("Códigos").setSortable(true);
        grid.addColumn(CourseEntity::getName).setKey("name").setHeader("Curso").setSortable(true);

        add(grid);
    }
}
