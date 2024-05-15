package com.example.lastone.model.mapper;

import com.example.lastone.model.dto.AddOrganizationProfileDTO;
import com.example.lastone.model.entity.OrganizationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    OrganizationEntity toOrganizationEntity(AddOrganizationProfileDTO organizationProfileDTO);
}
