package com.example.application.enums.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituationEnum {
    PENDING(10, "Pendente"),
    APPROVED(20, "Aprovado"),
    FINISHED(30, "Finalizado"),
    CANCELED(40, "Cancelado");

    private Integer code;
    private String description;

}
