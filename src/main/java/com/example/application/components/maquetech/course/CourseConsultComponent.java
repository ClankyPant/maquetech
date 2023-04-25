package com.example.application.components.maquetech.course;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.entities.course.CourseEntity;
import com.example.application.services.course.CourseService;
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
