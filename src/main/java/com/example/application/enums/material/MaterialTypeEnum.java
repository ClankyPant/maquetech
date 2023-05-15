package com.example.application.enums.material;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialTypeEnum {
    TOOL("Ferramenta"),
    CONSUMABLE("Meterial de consumo"),
    DESK("Material de escritório"),
    ENVIRONMENT("Reserva");

    private String description;
}
