package com.example.application.repositories.course;

import com.example.application.entities.course.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    @Query("""
                SELECT course
                FROM course_entity course
                WHERE course.name = ?1
            """)
    CourseEntity getByName(String name);
}
