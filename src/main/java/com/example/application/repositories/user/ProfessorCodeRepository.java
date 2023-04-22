package com.example.application.repositories.user;

import com.example.application.entities.user.ProfessorCodeEntity;
import com.example.application.entities.user.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorCodeRepository extends CrudRepository<ProfessorCodeEntity, Long> {

    @Query(value = """
                    SELECT p
                    FROM professor_code_entity p
                    WHERE p.code = ?1
                    """)
    public ProfessorCodeEntity getByCode(String code);
}
