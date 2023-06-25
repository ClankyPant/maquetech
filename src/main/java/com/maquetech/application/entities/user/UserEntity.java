package com.maquetech.application.entities.user;


import com.maquetech.application.entities.AbstractBean;
import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.entities.reservation.ReservationEntity;
import com.maquetech.application.enums.user.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(fetch = FetchType.EAGER)
    private CourseEntity course;

    @Column(columnDefinition = "boolean default true")
    private boolean isActive = true;

    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservationList;

    public UserTypeEnum getType() {
        return this.type != null ? this.type : UserTypeEnum.LEVEL_1;
    }

    public boolean isStudent() { return this.type.equals(UserTypeEnum.LEVEL_1); }

    public boolean isProfessor() { return this.type.equals(UserTypeEnum.LEVEL_2); }

    public boolean isAdmin() { return this.type.equals(UserTypeEnum.LEVEL_3); }

    public Boolean canSeeProfessorRegister() {
        return !this.isStudent();
    }

    public String getRoleStr() {
        String result = "NORMAL";
        if (this.isProfessor()) result = "PROFESSOR";
        if (this.isAdmin()) result = "ADMIN";

        return result;
    }
}
