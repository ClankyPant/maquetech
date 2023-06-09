package com.maquetech.application.services.user;

import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.repositories.user.UserRepository;
import com.maquetech.application.services.course.CourseService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final CourseService courseService;

    public UserService(@Autowired UserRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    public List<UserDetails> getDetailsList() {
        List<UserDetails> result = new ArrayList<>();
        List<UserEntity> userEntityIterable = this.repository.getOnlyActive();

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

    public List<UserModel> getSearchList(UserTypeEnum type, Long loggedId) {
        return UserHelper.transform(this.repository.getSearchList(loggedId, type));
    }

    public UserModel get(Long id) {
        var user = this.repository.findById(id).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("Erro ao buscar usuário!");
        }

        return UserHelper.transform(user);
    }

    public boolean hasByCpf(String cpf) {
        return this.getByCpf(cpf) != null;
    }

    public UserEntity getByCpf(String cpf) {
        return this.repository.findByCpf(cpf);
    }

    public boolean hasByUsername(String username) {
        return this.getByUsername(username) != null;
    }

    public UserEntity getByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public void save(UserModel model) {
        save(model, null);
    }

    public void save(UserModel model, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        var userEntity = this.repository.findById(ConvertHelper.getLong(model.getId(), -1L)).orElse(new UserEntity());
        UserHelper.transform(userEntity, model);

        if (model.isStudent()) {
            userEntity.setCourse(courseService.getByName(model.getCourse().getName()));
        }

        this.repository.save(userEntity);
        if (inMemoryUserDetailsManager != null) inMemoryUserDetailsManager.createUser(this.createUserDetail(userEntity));
    }

    public void changePassword(Long id, String newPassword, InMemoryUserDetailsManager inMemoryUserDetailsManager) {

        var user = this.repository.findById(id).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("Erro ao buscar usuário!");
        }

        user.setPassword(newPassword);
        this.repository.save(user);

        var userDetail = inMemoryUserDetailsManager.loadUserByUsername(user.getUsername());
        inMemoryUserDetailsManager.updatePassword(userDetail, "{bcrypt}"+newPassword);
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

    public void bulkChange(Set<UserModel> setUserModel, boolean situation, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        var idList = setUserModel.stream().map(UserModel::getId).toList();
        var listUser = this.repository.findAllById(idList);

        for (UserEntity entity : listUser) {
            entity.setActive(situation);
            this.repository.save(entity);

            var username = entity.getUsername();
            if (situation) {
                try {
                    inMemoryUserDetailsManager.loadUserByUsername(username);
                } catch (UsernameNotFoundException ex) {
                    inMemoryUserDetailsManager.createUser(this.createUserDetail(entity));
                }
            } else {
                inMemoryUserDetailsManager.deleteUser(username);
            }
        }
    }
}
