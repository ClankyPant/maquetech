package com.example.application.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    NORMAL(10,"Aluno"),
    PROFESSOR(20,"Professor");

    private Integer code;

    private String name;
}
