package com.example.application.entities.course;

import com.example.application.entities.AbstractBean;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "course_entity")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class CourseEntity extends AbstractBean {

    private String name;

}
