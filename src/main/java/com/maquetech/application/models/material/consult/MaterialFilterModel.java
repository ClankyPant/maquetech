package com.maquetech.application.models.material.consult;

import com.maquetech.application.enums.material.MaterialTypeEnum;
import com.maquetech.application.models.material.MaterialModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MaterialFilterModel {

    private MaterialTypeEnum type;

    private List<MaterialModel> materialModelList = new ArrayList<>();
}
