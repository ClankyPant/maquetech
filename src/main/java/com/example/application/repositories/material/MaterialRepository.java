package com.example.application.repositories.material;

import com.example.application.entities.material.MaterialEntity;
import com.example.application.enums.material.MaterialTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {

    @Query(
            value = """
                        SELECT mat
                        FROM material_entity mat
                        WHERE mat.onlyProfessor = false
                          AND (mat.id IN (:id) OR -1 IN (:id))
                          AND  (mat.type = :type OR :type IS NULL)
                    """
    )
    List<MaterialEntity> getMaterialForStudant(@Param("id") List<Long> idList, @Param("type") MaterialTypeEnum type);

    @Query(value = "SELECT mat FROM material_entity mat WHERE mat.onlyProfessor = false AND mat.name like ('%' || :consulting_term ||    '%')")
    Page<MaterialEntity> getMaterialForStudant(@Param("consulting_term") String consultingTerm, PageRequest page);

}
