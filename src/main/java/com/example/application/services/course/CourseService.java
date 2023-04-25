package com.example.application.services.course;

import com.example.application.entities.course.CourseEntity;
import com.example.application.repositories.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<CourseEntity> findAll() {
        return this.repository.findAll();
    }

    public void create(CourseEntity course) {
        this.repository.save(course);
    }

    public CourseEntity getByName(String name) {
        return this.repository.getByName(name);
    }

    public boolean hasByName(String name) {
        return getByName(name) != null;
    }
}
