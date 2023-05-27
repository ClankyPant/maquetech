package com.maquetech.application.helpers.user;

import com.maquetech.application.entities.course.CourseEntity;
import com.maquetech.application.models.user.CourseModel;

import java.util.List;

public class CourseHelper {
    private CourseHelper() {

    }

    public static List<CourseModel> transform(List<CourseEntity> entityList) {
        return entityList.stream().map(CourseHelper::transform).toList();
    }

    public static CourseModel transform(CourseEntity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null!");

        return CourseModel
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static CourseEntity transform(CourseModel model) {
        var result = CourseEntity
                .builder()
                .name(model.getName())
                .build();

        result.setId(model.getId());
        return result;
    }
}
