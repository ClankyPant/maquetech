package com.example.application.enums.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialTypeEnum {
    NORMAL("Normal"),
    CONSUMABLE("Consumivel"),
    COLLECTION("Acervo"),
    ENVIRONMENT("Ambiente");

    private String description;
}
