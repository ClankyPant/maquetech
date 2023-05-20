package com.maquetech.application.services.material;

import com.maquetech.application.entities.material.MaterialEntity;
import com.maquetech.application.enums.material.MaterialTypeEnum;
import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.repositories.material.MaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class MaterialService {

    private final MaterialRepository repository;

    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    public void create(MaterialEntity materialEntity) {
        this.repository.save(materialEntity);
    }

    public List<MaterialModel> getListByPage(String term, UserTypeEnum userTypeEnum, PageRequest pageRequest) {
        return entityToModel(this.getPageByUser(term, userTypeEnum, pageRequest).getContent());
    }

    public List<MaterialModel> getList(List<Long> idList, MaterialTypeEnum type, UserTypeEnum userTypeEnum, boolean isOnlyConsult) {
        if (isOnlyConsult) {
            return getListForConsult(idList, type, userTypeEnum);
        }

        return getListForReservation(idList, type, userTypeEnum);
    }

    public List<MaterialModel> getListForConsult(List<Long> idList, MaterialTypeEnum type, UserTypeEnum userTypeEnum) {
        return this.entityToModel(getListByUser(idList, type, userTypeEnum));
    }

    public List<MaterialModel> getListForReservation(List<Long> idList, MaterialTypeEnum type, UserTypeEnum userTypeEnum) {
        var result = this.entityToModel(getListByUser(idList, type, userTypeEnum));
        return result.stream()
                .filter(material -> material.getStockQty() > 0)
                .toList();
    }

    private List<MaterialModel> entityToModel(List<MaterialEntity> entityList) {
        return entityList.stream().map(this::entityToModel).toList();
    }

    private MaterialModel entityToModel(MaterialEntity entity) {
        return MaterialModel
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .unit(entity.getUnit())
                .type(entity.getType())
                .stockQty(entity.getStockQty())
                .location(entity.getLocation())
                .stockSafeQty(entity.getStockSafeQty())
                .build();
    }

    private Page<MaterialEntity> getPageByUser(String term, UserTypeEnum userTypeEnum, PageRequest pageRequest) {
        if (UserTypeEnum.LEVEL_1.equals(userTypeEnum)) {
            return this.repository.getMaterialStudent(term, pageRequest);
        }

        return this.repository.getMaterial(term, pageRequest);

    }

    private List<MaterialEntity> getListByUser(List<Long> idList, MaterialTypeEnum type, UserTypeEnum userTypeEnum) {
        if (CollectionUtils.isEmpty(idList)) idList = List.of(-1L);
        var idStrList = idList.stream().map(String::valueOf).toList();
        var typeStr = Objects.nonNull(type) ? type.name() : null;

        if (UserTypeEnum.LEVEL_1.equals(userTypeEnum)) {
            return this.repository.getMaterialStudent(idStrList, typeStr);
        }

        return this.repository.getMaterial(idStrList, typeStr);
    }

    public MaterialEntity getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
