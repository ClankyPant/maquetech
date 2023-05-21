package com.maquetech.application.models.reservation;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationMaterialModel {

    private Long materialId;

    private Double quantity;

    private String materialName;
}

