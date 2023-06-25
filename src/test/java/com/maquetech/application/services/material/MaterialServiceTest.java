package com.maquetech.application.services.material;

import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.entities.material.MaterialEntity;
import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.repositories.course.CourseRepository;
import com.maquetech.application.repositories.material.MaterialRepository;
import com.maquetech.application.services.course.CourseService;
import com.maquetech.application.services.reservation.ReservationService;
import com.vaadin.flow.theme.material.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MaterialServiceTest {
    @InjectMocks
    private MaterialService service;

    @Mock
    private ReservationService reservationService;

    @Mock
    private MaterialRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void getListMustNotReturnNull() {
        Mockito.when(repository.getMaterialStudent(List.of(), null)).thenReturn(List.of());
        Mockito.when(repository.getMaterial(List.of(), null)).thenReturn(List.of());
        Mockito.when(reservationService.getOnReservationMap(null, null)).thenReturn(new HashMap<>());
        Mockito.when(repository.getMaterial(List.of("-1"), null)).thenReturn(List.of(new MaterialEntity()));
        Mockito.when(repository.getMaterialStudent(List.of(), null)).thenReturn(List.of(new MaterialEntity()));

        service.getList(List.of(), null, UserTypeEnum.LEVEL_1, true, null, null);
        service.getList(List.of(), null, UserTypeEnum.LEVEL_1, false, null, null);
        service.getList(List.of(), null, UserTypeEnum.LEVEL_1, false, new Date(), new Date());
    }

    @Test
    void saveMustSaveOneRegister() {
        var entity = new MaterialEntity();
        service.save(entity);
        verify(repository, times(1)).saveAll(List.of(entity));
    }
}