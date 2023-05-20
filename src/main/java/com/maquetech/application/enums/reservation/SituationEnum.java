package com.maquetech.application.enums.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituationEnum {
    PENDING(10, "Pendente"),
    APPROVED(20, "Aprovado"),
    IN_PROGRESS(30, "Em andamento"),
    FINISHED(40, "Finalizado"),
    CANCELED(50, "Cancelado");

    private Integer code;
    private String description;

}
