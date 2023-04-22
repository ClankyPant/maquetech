package com.example.application.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    NIVEL_1("Aluno"),
    NIVEL_2("Professor");

    private String description;
}
