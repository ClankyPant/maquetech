package com.example.application.enums.material;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialUnitEnum {
    UN("Unidade (UN)"),
    BX("Caixa (CX)"),
    ML("Mililitros (ML)"),
    MG("Miligrama (MG)");

    private String description;
}
