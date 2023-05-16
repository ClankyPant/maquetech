package com.example.application.enums.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum MaterialTypeEnum {
    TOOL("Ferramenta"),
    CONSUMABLE("Meterial de consumo"),
    DESK("Material de escritório"),
    ENVIRONMENT("Reserva");

    private String description;
}
