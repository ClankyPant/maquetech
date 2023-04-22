package com.example.application.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    LEVEL_1("Aluno"),
    LEVEL_2("Professor"),
    LEVEL_3("Laboratorista");

    private String description;
}
