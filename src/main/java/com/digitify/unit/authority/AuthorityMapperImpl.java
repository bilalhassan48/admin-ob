package com.digitify.unit.authority;

import com.digitify.framework.annotation.Mapper;

import java.util.UUID;

@Mapper
public class AuthorityMapperImpl implements AuthorityMapper {
    @Override
    public Authority toEntity(AuthorityDto dto) {
        Authority entity = new Authority();
        entity.setUuid(UUID.randomUUID().toString());
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public Authority toCleanEntity(CleanAuthorityDto dto) {
        Authority entity = new Authority();
        entity.setUuid(dto.getUuid());
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsAdmin(dto.getIsAdmin());
        return entity;
    }

    @Override
    public AuthorityDto toDto(Authority entity) {
        AuthorityDto dto = new AuthorityDto();
        dto.setUuid(entity.getUuid());
        dto.setIsActive(entity.getIsActive());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    @Override
    public Authority update(AuthorityDto dto, Authority entity) {
        entity.setIsActive(dto.getIsActive());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public Authority partialUpdate(CleanAuthorityDto dto, Authority entity) {
        if (isNotEmpty(dto.getIsActive())) {
            entity.setIsActive(dto.getIsActive());
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
