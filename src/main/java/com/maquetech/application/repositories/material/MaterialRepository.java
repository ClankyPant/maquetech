package com.maquetech.application.repositories.material;

import com.maquetech.application.entities.material.MaterialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {

    @Query(
            value = """
                        SELECT mat.*
                        FROM material_entity mat
                        WHERE mat.only_professor = false
                              AND (CAST(mat.id AS VARCHAR(255)) IN (:id) OR '-1' IN (:id))
                              AND (mat.type = :type OR :type IS NULL)
                    """, nativeQuery = true
    )
    List<MaterialEntity> getMaterialStudent(@Param("id") List<String> idList, @Param("type") String type);

    @Query(value = "SELECT mat FROM material_entity mat WHERE mat.onlyProfessor = false AND mat.name like ('%' || :consulting_term || '%')")
    Page<MaterialEntity> getMaterialStudent(@Param("consulting_term") String consultingTerm, PageRequest page);


    @Query(
            value = """
                        SELECT mat.*
                        FROM material_entity mat
                        WHERE (CAST(mat.id AS VARCHAR(255)) IN (:id) OR '-1' IN (:id))
                              AND (mat.type = :type OR :type IS NULL)
                    """, nativeQuery = true
    )
    List<MaterialEntity> getMaterial(@Param("id") List<String> idList, @Param("type") String type);

    @Query(value = "SELECT mat FROM material_entity mat WHERE mat.name like ('%' || :consulting_term || '%')")
    Page<MaterialEntity> getMaterial(@Param("consulting_term") String consultingTerm, PageRequest page);
}
