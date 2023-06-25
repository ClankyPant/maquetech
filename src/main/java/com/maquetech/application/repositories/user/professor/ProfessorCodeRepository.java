package com.maquetech.application.repositories.user.professor;

import com.maquetech.application.entities.user.professor.ProfessorCodeEntity;
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
    ProfessorCodeEntity getByCode(String code);
}
