package com.example.application.entities.usuario;


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
public class UsuarioEntity extends AbstractBean {

    @Column(nullable = false)
    private String username;

    private String nome;

    @Column(nullable = false)
    private Integer tipo;

    private String email;

    @Column(nullable = false, columnDefinition = "text")
    private String senha;

    @Column(nullable = false)
    private String cpf;

    private String telefone;
}
