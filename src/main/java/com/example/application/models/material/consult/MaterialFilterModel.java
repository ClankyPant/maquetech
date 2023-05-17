package com.example.application.models.material.consult;

import com.example.application.enums.material.MaterialTypeEnum;
import com.example.application.models.material.MaterialModel;
import lombok.Builder;
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
