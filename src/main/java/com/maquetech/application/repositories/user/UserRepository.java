package com.maquetech.application.repositories.user;

import com.maquetech.application.entities.user.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("""
           SELECT user
           FROM user_entity user
           WHERE user.username = ?1
           """)
    UserEntity findByUsername(String username);


    @Query(value = """
           SELECT us
           FROM user_entity us
           WHERE us.id <> ?1
           ORDER BY us.type
           """)
    List<UserEntity> getSearchList(Long loggedId);
}
