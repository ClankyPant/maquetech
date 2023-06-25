package com.maquetech.application.services.user.professor;

import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.entities.user.professor.ProfessorCodeEntity;
import com.maquetech.application.repositories.course.CourseRepository;
import com.maquetech.application.repositories.user.professor.ProfessorCodeRepository;
import com.maquetech.application.services.course.CourseService;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProfessorCodeServiceTest {

    @InjectMocks
    private ProfessorCodeService service;

    @Mock
    private ProfessorCodeRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void isValidCodeReturnRight() throws NotFoundException {
        final var CODE = "10";
        final var WRONG_CODE = "20";

        Mockito.when(repository.getByCode(CODE)).thenReturn(new ProfessorCodeEntity(CODE, true));
        assertTrue(service.isValidCode(CODE));

        Mockito.when(repository.getByCode(CODE)).thenReturn(new ProfessorCodeEntity(CODE, false));
        assertFalse(service.isValidCode(CODE));

        assertThrows(NotFoundException.class, () -> service.isValidCode(WRONG_CODE));
    }

    @Test
    void invalidateCodeMustInavalidate() throws NotFoundException {
        final var CODE = "10";
        var entity = new ProfessorCodeEntity(CODE, true);

        Mockito.when(repository.getByCode(CODE)).thenReturn(entity);
        service.invalidateCode(CODE);
    }
}