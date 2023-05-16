package com.example.application.models.material.consult;

import com.example.application.enums.material.MaterialTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialFilterModel {

    private Long code;
    private String name;
    private MaterialTypeEnum type;
}
