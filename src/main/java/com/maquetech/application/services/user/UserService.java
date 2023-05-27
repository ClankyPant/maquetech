package com.maquetech.application.services.user;

import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.repositories.user.UserRepository;
import com.maquetech.application.services.course.CourseService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final CourseService courseService;

    public UserService(@Autowired UserRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    public List<UserDetails> getAll() {
        List<UserDetails> result = new ArrayList<>();
        Iterable<UserEntity> userEntityIterable = this.repository.findAll();

        for (UserEntity userIterable : userEntityIterable) {
            result.add(createUserDetail(userIterable));
        }

        return result;
    }

    public UserDetails createUserDetail(UserEntity userEntity) {
        return User
                .withUsername(userEntity.getUsername())
                .password("{bcrypt}"+userEntity.getPassword())
                .roles(userEntity.getRoleStr())
                .build();
    }

    public UserDetails createUserDetail(UserModel userModel) {
        return User
                .withUsername(userModel.getUsername())
                .password("{bcrypt}"+userModel.getPassword())
                .roles(userModel.getRoleStr())
                .build();
    }

    public boolean hasByUsername(String username) {
        return this.getByUsername(username) != null;
    }

    public UserEntity getByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public void save(UserModel model) {
        var userEntity = this.repository.findById(ConvertHelper.getLong(model.getId(), -1L)).orElse(new UserEntity());
        UserHelper.transform(userEntity, model);

        if (model.isStudent()) {
            userEntity.setCourse(courseService.getByName(model.getCourse().getName()));
        }

        this.repository.save(userEntity);
    }

    public UserEntity getLoggedUser() throws NotFoundException {
        UserEntity result = null;
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = context.getAuthentication().getName();
            result = this.getByUsername(username);
        }

        if (result == null) throw new NotFoundException("Usuário logado inválido!");

        return result;
    }
}
