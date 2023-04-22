package com.example.application.entities.user;


import com.example.application.entities.AbstractBean;
import com.example.application.entities.course.CourseEntity;
import com.example.application.enums.user.UserTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "user_entity")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class UserEntity extends AbstractBean {

    @Column(nullable = false)
    private String username;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserTypeEnum type = UserTypeEnum.LEVEL_1;

    private String mail;

    @Column(nullable = false, columnDefinition = "text")
    private String password;

    @Column(nullable = false)
    private String cpf;

    private String phone;

    @OneToOne
    private CourseEntity course;

    public UserTypeEnum getType() {
        return this.type != null ? this.type : UserTypeEnum.LEVEL_1;
    }

    public boolean isStudent() { return this.type.equals(UserTypeEnum.LEVEL_1); }

    public boolean isProfessor() { return this.type.equals(UserTypeEnum.LEVEL_2); }

    public boolean isAdmin() { return this.type.equals(UserTypeEnum.LEVEL_3); }

    public String getRoleStr() {
        String result = "NORMAL";
        if (this.isProfessor()) result = "PROFESSOR";
        if (this.isAdmin()) result = "ADMIN";

        return result;
    }
}
