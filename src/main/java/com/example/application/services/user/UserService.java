package com.example.application.services.user;

import com.example.application.entities.user.UserEntity;
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
            result.add(
                    User
                    .withUsername(userIterable.getUsername())
                    .password("{bcrypt}"+userIterable.getPassword())
                    .roles("USER")
                    .build()
            );
        }

        return result;
    }

}
