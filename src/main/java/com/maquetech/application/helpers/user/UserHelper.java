package com.maquetech.application.helpers.user;

import com.maquetech.application.Application;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.models.user.UserModel;
import javassist.NotFoundException;

import java.util.List;

public class UserHelper {

    public static UserEntity getLoggedUser() throws NotFoundException {
        return Application.USER_SERVICE.getLoggedUser();
    }

    public static UserModel getLoggerUserModel() throws NotFoundException {
        return UserHelper.transform(Application.USER_SERVICE.getLoggedUser());
    }

    public static void logout() {
        Application.AUTHENTICATION_CONTEXT.logout();
    }

    public static UserEntity transform(UserEntity result, UserModel model) {
        result.setCpf(model.getCpf());
        result.setMail(model.getMail());
        result.setName(model.getName());
        result.setType(model.getType());
        result.setPhone(model.getPhone());
        result.setActive(model.isActive());
        result.setUsername(model.getUsername());
        result.setPassword(model.getPassword());
        if (model.isStudent()) result.setCourse(CourseHelper.transform(model.getCourse()));

        return result;
    }

    public static UserModel transform(UserEntity entity) {
        var result = UserModel
                .builder()
                .cpf(entity.getCpf())
                .name(entity.getName())
                .type(entity.getType())
                .mail(entity.getMail())
                .phone(entity.getPhone())
                .isActive(entity.isActive())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .course(CourseHelper.transform(entity.getCourse()))
                .build();

        result.setId(entity.getId());
        return result;
    }

    public static List<UserModel> transform(List<UserEntity> entityList) {
        return entityList.stream().map(UserHelper::transform).toList();
    }
}
