package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.ResponseAsListDTO;
import com.example.lastone.model.entity.XRayEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface XRayMapper {
    ResponseAsListDTO toXRayAsListDto(XRayEntity xRay);
}
