package com.example.application.services.user;

import com.example.application.entities.user.UserEntity;
import com.example.application.enums.user.UserTypeEnum;
import com.example.application.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
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
                .roles(userEntity.getType().equals(UserTypeEnum.NIVEL_1) ? "USER" : "PROFESSOR")
                .build();
    }

    public boolean hasByUsername(String username) {
        return this.getByUsername(username) != null;
    }

    public UserEntity getByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    public void save(UserEntity userEntity) {
        this.repository.save(userEntity);
    }
}
