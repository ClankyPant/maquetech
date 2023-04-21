package com.example.application.services.usuario;

import com.example.application.entities.usuario.UsuarioEntity;
import com.example.application.repositories.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(@Autowired UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UserDetails> getAll() {
        List<UserDetails> result = new ArrayList<>();
        Iterable<UsuarioEntity> userEntityIterable = this.repository.findAll();

        for (UsuarioEntity userIterable : userEntityIterable) {
            result.add(
                    User
                    .withUsername(userIterable.getUsername())
                    .password("{bcrypt}"+userIterable.getSenha())
                    .roles("USER")
                    .build()
            );
        }

        return result;
    }

}
