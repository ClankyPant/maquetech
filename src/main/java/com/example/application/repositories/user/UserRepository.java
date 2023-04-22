package com.example.application.repositories.user;

import com.example.application.entities.user.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("""
           SELECT user
           FROM user_entity user
           WHERE user.username = ?1
           """)
    public UserEntity findByUsername(String username);
}
