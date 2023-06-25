package com.maquetech.application.services.course;

import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.repositories.course.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void getListMustNotBeNull() {
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());
        assertNotNull(courseService.getList());
    }

    @Test
    void createMustSave() {
        var couseEntity = new CourseEntity();
        courseService.create(couseEntity);
        verify(repository, times(1)).save(couseEntity);
    }
}