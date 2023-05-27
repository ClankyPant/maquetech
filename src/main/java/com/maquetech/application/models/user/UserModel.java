package com.maquetech.application.models.user;

import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.models.reservation.ReservationModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private Long id;

    private String mail;
    private String name;
    private String cpf;
    private String phone;
    private String username;
    private String password;
    private CourseModel course;
    private List<ReservationModel> reservationList;

    private UserTypeEnum type = UserTypeEnum.LEVEL_1;

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
