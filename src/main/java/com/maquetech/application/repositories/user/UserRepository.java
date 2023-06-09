package com.maquetech.application.repositories.user;

import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.user.UserTypeEnum;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM user_entity WHERE is_active = true", nativeQuery = true)
    List<UserEntity> getOnlyActive();

    @Query("""
           SELECT user
           FROM user_entity user
           WHERE user.username = ?1
           """)
    UserEntity findByUsername(String username);

    @Query("""
           SELECT user
           FROM user_entity user
           WHERE user.cpf = ?1
           """)
    UserEntity findByCpf(String cpf);

    @Query(value = """
           SELECT us
           FROM user_entity us
           WHERE us.id <> ?1
             AND (us.type = ?2 OR ?2 IS NULL)
           ORDER BY us.type
           """)
    List<UserEntity> getSearchList(Long loggedId, UserTypeEnum type);
}
