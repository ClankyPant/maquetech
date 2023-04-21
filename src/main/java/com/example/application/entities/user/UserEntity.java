package com.example.application.entities.user;


import com.example.application.entities.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class UserEntity extends AbstractBean {

    @Column(nullable = false)
    private String username;

    private String name;

    @Column(nullable = false)
    private Integer type;

    private String mail;

    @Column(nullable = false, columnDefinition = "text")
    private String password;

    @Column(nullable = false)
    private String cpf;

    private String phone;
}
