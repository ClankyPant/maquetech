package com.maquetech.application.entities.course;

import com.maquetech.application.entities.AbstractBean;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "course_entity")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class CourseEntity extends AbstractBean {

    private String name;

}
