package com.example.application.entities.user;

import com.example.application.entities.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "professor_code_entity")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "code" }) })
public class ProfessorCodeEntity extends AbstractBean {

    @Column(nullable = false, columnDefinition = "text")
    private String code;

    @Column(nullable = false)
    private Boolean isValid = true;
}
