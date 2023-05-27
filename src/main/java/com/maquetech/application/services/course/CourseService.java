package com.maquetech.application.services.course;

import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.helpers.user.CourseHelper;
import com.maquetech.application.models.user.CourseModel;
import com.maquetech.application.repositories.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<CourseModel> getList() {
        return CourseHelper.transform(this.repository.findAll());
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
