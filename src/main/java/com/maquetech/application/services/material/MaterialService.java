package com.maquetech.application.services.material;

import com.maquetech.application.entities.material.MaterialEntity;
import com.maquetech.application.enums.material.MaterialTypeEnum;
import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.helpers.material.MaterialHelper;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.repositories.material.MaterialRepository;
import com.maquetech.application.services.reservation.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    private final MaterialRepository repository;
    private final ReservationService reservationService;

    public MaterialService(MaterialRepository repository, ReservationService reservationService) {
        this.repository = repository;
        this.reservationService = reservationService;
    }

    public void save(MaterialEntity materialEntity) {
        save(List.of(materialEntity));
    }

    public void save(List<MaterialEntity> materialEntityList) {
        this.repository.saveAll(materialEntityList);
    }

    public List<MaterialModel> getListByPage(String term, UserTypeEnum userTypeEnum, PageRequest pageRequest) {
        return MaterialHelper.transform(getPageByUser(term, userTypeEnum, pageRequest).getContent());
    }

    public List<MaterialModel> getList(List<Long> idList, MaterialTypeEnum type,
                                       UserTypeEnum userTypeEnum, boolean isOnlyConsult,
                                       Date startBookingDate, Date endBookingDate) {
        if (isOnlyConsult) {
            return getListForConsult(idList, type, userTypeEnum);
        }

        return getListForReservation(idList, type, userTypeEnum, startBookingDate, endBookingDate);
    }

    private List<MaterialModel> getListForConsult(List<Long> idList, MaterialTypeEnum type, UserTypeEnum userTypeEnum) {
        return MaterialHelper.transform(getListByUser(idList, type, userTypeEnum));
    }

    private List<MaterialModel> getListForReservation(List<Long> idList, MaterialTypeEnum type, UserTypeEnum userTypeEnum,
                                                     Date startBookingDate, Date endBookingDate) {

        var materialModelList = MaterialHelper.transform(getListByUser(idList, type, userTypeEnum));

        if (Objects.nonNull(startBookingDate) && Objects.nonNull(endBookingDate)) {
            var onReservationMap = reservationService.getOnReservationMap(startBookingDate, endBookingDate);

            for (var materialModel : materialModelList) {
                var materialModelId = materialModel.getId();

                if (onReservationMap.containsKey(materialModelId)) {
                    var materialStock = materialModel.getStockQty();
                    materialModel.setStockQty(materialStock - onReservationMap.get(materialModelId));
                }
            }
        }

        return materialModelList;
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

    public void validateAndRemoveConsumables(Long id) {
        var reservation = reservationService.getById(id);
        var reservationMaterialList = reservation.getMaterialList();
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(reservationMaterialList)) {
            throw new IllegalArgumentException("Erro ao validar materiais da reserva!");
        }

        for (var reservationMaterial : reservationMaterialList) {
            var material = reservationMaterial.getMaterial();
            if (material == null) {
                throw new IllegalArgumentException("Erro ao validar materiais da reserva!");
            }

            if (material.isConsumable()) {
                material.setStockQty(material.getStockQty() - reservationMaterial.getQuantity());
            }

            this.save(material);
        }
    }

    public Map<Long, MaterialEntity> getMapById(List<Long> idList) {
        return this.repository.getList(idList).stream().collect(Collectors.toMap(MaterialEntity::getId, value -> value));
    }
}
